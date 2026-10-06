package com.example.mock.order.persistence.dao.jooq;

import com.example.mock.order.common.auth.AttributeType;
import com.example.mock.order.common.auth.AuthAdapter;
import com.example.mock.order.common.auth.DataType;
import com.example.mock.order.common.dao.BaseStoredProcedureResult;
import com.example.mock.order.common.dao.DaoException;
import com.example.mock.order.common.dao.StoredProcedureResultTemplate;
import com.example.mock.order.common.dao.StoredProcedureUtil;
import com.example.mock.order.common.datasource.DataSourceConstant;
import com.example.mock.order.common.util.HasherUtil;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Configuration;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Results;
import org.jooq.Routine;
import org.jooq.impl.AbstractRoutine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Base class for DAOs. Every database call goes through a jOOQ-generated stored procedure routine.
 * Write routines run on the primary connection, read routines on the read replica.
 */
@Slf4j
public abstract class AbstractJooqDao {

    /** Stored procedures return code 20000 in their first result row on success. */
    private static final int SUCCESS_CODE = 20000;
    private static final String AUTO_COMMIT = "AUTO_COMMIT";

    protected static final DataType ORDER_DATA_TYPE = DataType.ORDER;

    @Value("${useWriteJooqConfigurationOnReadWrite:false}")
    private Boolean useWriteJooqConfigurationOnReadWrite;

    @Autowired
    @Qualifier(DataSourceConstant.READ_JOOQ_CONFIGURATION)
    private Configuration readConfiguration;

    @Autowired
    @Qualifier(DataSourceConstant.WRITE_JOOQ_CONFIGURATION)
    private Configuration writeConfiguration;

    @SuppressWarnings("rawtypes")
    private final BaseStoredProcedureResult baseStoredProcedureResult = new BaseStoredProcedureResult();

    @Autowired
    private AuthAdapter authAdapter;

    protected <T extends AbstractRoutine<Void>> void executeWithWriteConfig(T routine) {
        routine.execute(writeConfiguration);
    }

    protected <T extends AbstractRoutine<Void>> void executeWithReadConfig(T routine) {
        boolean hasAutoCommit = Arrays.stream(routine.getClass().getFields())
                .anyMatch(f -> AUTO_COMMIT.equals(f.getName()));
        if (hasAutoCommit) {
            throw new IllegalStateException("Write routines (with AUTO_COMMIT) must not run on the read connection.");
        }
        routine.execute(useWriteJooqConfigurationOnReadWrite ? writeConfiguration : readConfiguration);
    }

    /**
     * Instantiates a routine and fills the parameters every stored procedure expects from the caller's token:
     * company, employee and data rights. Callers set only the business parameters.
     */
    protected <V, T extends Routine<V>> T createRoutinesWithAuth(Class<T> clazz, DataType dataType) {
        try {
            T routine = clazz.getDeclaredConstructor().newInstance();

            Long companyId = HasherUtil.unhash((String) authAdapter.getAttribute(AttributeType.COMPANY_ID));
            clazz.getMethod("setInCompanyId", org.jooq.types.UInteger.class)
                    .invoke(routine, StoredProcedureUtil.longToUInt(companyId));

            Long empUserId = HasherUtil.unhash((String) authAdapter.getAttribute(AttributeType.EMPLOYEE_ID));
            clazz.getMethod("setInEmpUserId", org.jooq.types.UInteger.class)
                    .invoke(routine, StoredProcedureUtil.longToUInt(empUserId));

            Method setDataRights = clazz.getMethod("setInEmpDataRights", org.jooq.types.UInteger.class);
            setDataRights.invoke(routine, StoredProcedureUtil.integerToUInteger(authAdapter.getPermission(dataType.getValue())));
            return routine;
        } catch (ReflectiveOperationException e) {
            log.error("Cannot create routine {}", clazz.getName(), e);
            throw new DaoException(e);
        }
    }

    protected <T extends StoredProcedureResultTemplate, V> List<T> getListResultWithValidation(Routine<V> routine, Class<T> clazz) {
        List<T> rows = transferResults(routine, clazz);
        if (!rows.isEmpty()) {
            validate(rows.get(0));
        } else {
            log.warn("Stored procedure returned no rows");
        }
        return rows;
    }

    protected <T extends StoredProcedureResultTemplate, V> T getOneResultWithValidation(Routine<V> routine, Class<T> clazz) {
        List<T> rows = getListResultWithValidation(routine, clazz);
        return rows.isEmpty() ? null : rows.get(0);
    }

    private void validate(StoredProcedureResultTemplate row) {
        if (row.getCode() != null && row.getCode() != SUCCESS_CODE) {
            log.error("Stored procedure returned code {} sub_code {}: {}", row.getCode(), row.getSubCode(), row.getErrMsg());
            throw new DaoException(row.getCode(), row.getErrMsg(), row.getSubCode());
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends StoredProcedureResultTemplate, V> List<T> transferResults(Routine<V> routine, Class<T> clazz) {
        Results results = routine.getResults();
        if (CollectionUtils.isEmpty(results)) {
            return Collections.emptyList();
        }
        Result<Record> first = results.get(0);
        return first.map(baseStoredProcedureResult.getMapper(clazz));
    }
}
