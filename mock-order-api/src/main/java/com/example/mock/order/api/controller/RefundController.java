package com.example.mock.order.api.controller;

import com.example.mock.order.operation.RefundOperationHandler;
import com.example.mock.order.view.swagger.api.RefundApi;
import com.example.mock.order.view.swagger.model.RefundRequestBody;
import com.example.mock.order.view.swagger.model.RefundResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class RefundController implements RefundApi {

    @Autowired
    private RefundOperationHandler refundOperationHandler;

    @Override
    public ResponseEntity<RefundResponseBody> refundsPost(@Valid @RequestBody RefundRequestBody body) {
        return refundOperationHandler.execute(body.getOpCode().toString(), body);
    }
}
