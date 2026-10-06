package com.example.mock.order.operation;

import org.springframework.http.ResponseEntity;

public interface DispatchOperation<T, R> {

    ResponseEntity<R> execute(String opCode, T requestData);
}
