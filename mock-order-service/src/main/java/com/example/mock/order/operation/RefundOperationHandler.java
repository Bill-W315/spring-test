package com.example.mock.order.operation;

import com.example.mock.order.entity.Refund;
import com.example.mock.order.service.RefundService;
import com.example.mock.order.translator.RefundTranslator;
import com.example.mock.order.util.ResponseUtil;
import com.example.mock.order.view.swagger.model.RefundAddData;
import com.example.mock.order.view.swagger.model.RefundRequestBody;
import com.example.mock.order.view.swagger.model.RefundResponseBody;
import com.example.mock.order.common.util.HasherUtil;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RefundOperationHandler extends GenericOperationHandler<RefundRequestBody, RefundResponseBody> {

    @Autowired
    private RefundService refundService;

    @Override
    public ResponseEntity<RefundResponseBody> executeGetAction(RefundRequestBody requestData) {
        Preconditions.checkNotNull(requestData.getGetData(), GET_DATA_REQUIRED_MSG);
        Long orderId = HasherUtil.unhash(requestData.getGetData().getOrderId());

        List<Refund> refunds = refundService.findByOrderId(orderId);
        return ResponseUtil.createSuccessResponse(refunds, RefundTranslator::toViewModel, RefundResponseBody.class);
    }

    @Override
    public ResponseEntity<RefundResponseBody> executeAddAction(RefundRequestBody requestData) {
        RefundAddData data = Preconditions.checkNotNull(requestData.getAddData(), ADD_DATA_REQUIRED_MSG);
        Long orderId = HasherUtil.unhash(data.getOrderId());

        List<Refund> refunds = refundService.refund(orderId, data.getAmount(), data.getReason());
        return ResponseUtil.createSuccessResponse(refunds, RefundTranslator::toViewModel, RefundResponseBody.class);
    }
}
