package com.example.mock.order.common.dao;

import org.jooq.Field;
import org.jooq.Record;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * One row of a stored-procedure result set. Every procedure returns {@code code} (20000 = success),
 * {@code err_msg} and {@code sub_code}; subclasses map their business columns in {@link #doSuccessProcess}.
 */
public abstract class StoredProcedureResultTemplate {

    public static final int SUCCESS_CODE = 20000;

    private Integer code;
    private String errMsg;
    private String subCode;

    public abstract void doSuccessProcess(Record record);

    /** Maps the status columns, then the business columns when the procedure succeeded. */
    public void fill(Record record) {
        this.code = getIntegerField(record, "code");
        this.errMsg = getStringField(record, "err_msg");
        this.subCode = getStringField(record, "sub_code");
        if (code == null || code == SUCCESS_CODE) {
            doSuccessProcess(record);
        }
    }

    public Integer getCode() {
        return code;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public String getSubCode() {
        return subCode;
    }

    protected Long getLongField(Record record, String column) {
        Object value = value(record, column);
        return value == null ? null : ((Number) value).longValue();
    }

    protected Integer getIntegerField(Record record, String column) {
        Object value = value(record, column);
        return value == null ? null : ((Number) value).intValue();
    }

    protected String getStringField(Record record, String column) {
        Object value = value(record, column);
        return value == null ? null : value.toString();
    }

    protected BigDecimal getBigDecimalField(Record record, String column) {
        Object value = value(record, column);
        return value == null ? null : new BigDecimal(value.toString());
    }

    protected LocalDateTime getLocalDateTimeField(Record record, String column) {
        Object value = value(record, column);
        return value instanceof java.sql.Timestamp ? ((java.sql.Timestamp) value).toLocalDateTime() : (LocalDateTime) value;
    }

    private static Object value(Record record, String column) {
        Field<?> field = record.field(column);
        return field == null ? null : record.get(field);
    }
}
