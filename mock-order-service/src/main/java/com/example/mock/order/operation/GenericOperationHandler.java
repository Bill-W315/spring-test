package com.example.mock.order.operation;

import com.example.mock.order.model.exception.NotImplementedException;
import org.springframework.http.ResponseEntity;

/**
 * Routes a request to the action selected by the {@code op_code} field of the request body.
 * Concrete handlers override only the actions their API supports.
 */
public class GenericOperationHandler<T, R> implements DispatchOperation<T, R> {

    static final String GET_DATA_REQUIRED_MSG = "get_data must not be null";
    static final String ADD_DATA_REQUIRED_MSG = "add_data must not be null";
    static final String CANCEL_DATA_REQUIRED_MSG = "cancel_data must not be null";

    @Override
    public ResponseEntity<R> execute(String opCode, T requestData) {
        switch (opCode) {
            case "get":
                return executeGetAction(requestData);
            case "add":
                return executeAddAction(requestData);
            case "cancel":
                return executeCancelAction(requestData);
            default:
                throw new NotImplementedException();
        }
    }

    public ResponseEntity<R> executeGetAction(T requestData) {
        throw new NotImplementedException();
    }

    public ResponseEntity<R> executeAddAction(T requestData) {
        throw new NotImplementedException();
    }

    public ResponseEntity<R> executeCancelAction(T requestData) {
        throw new NotImplementedException();
    }
}
