package com.example.mock.order.common.dao;

/** A stored procedure returned a non-success code, or a routine could not be created or executed. */
public class DaoException extends RuntimeException {

    private final Integer code;
    private final String subCode;

    public DaoException(Throwable cause) {
        super(cause);
        this.code = 50000;
        this.subCode = null;
    }

    public DaoException(Integer code, String message, String subCode) {
        super(message);
        this.code = code;
        this.subCode = subCode;
    }

    public Integer getCode() {
        return code;
    }

    public String getSubCode() {
        return subCode;
    }
}
