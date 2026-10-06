package com.example.mock.order.model.exception;

public class NotImplementedException extends RuntimeException {

    public NotImplementedException() {
        super("op_code is not supported by this API");
    }
}
