package com.example.mock.order.common.dao;

import org.jooq.Record;
import org.jooq.RecordMapper;

/** Maps stored-procedure result rows to {@link StoredProcedureResultTemplate} subclasses. */
public class BaseStoredProcedureResult<T extends StoredProcedureResultTemplate> {

    public <R extends StoredProcedureResultTemplate> RecordMapper<Record, R> getMapper(Class<R> clazz) {
        return record -> {
            try {
                R row = clazz.getDeclaredConstructor().newInstance();
                row.fill(record);
                return row;
            } catch (ReflectiveOperationException e) {
                throw new DaoException(e);
            }
        };
    }
}
