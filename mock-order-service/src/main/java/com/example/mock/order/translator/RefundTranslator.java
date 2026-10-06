package com.example.mock.order.translator;

import com.example.mock.order.entity.Refund;
import com.example.mock.order.view.swagger.model.RefundModel;
import com.example.mock.order.common.util.HasherUtil;

public class RefundTranslator {

    public static RefundModel toViewModel(Refund source) {
        RefundModel target = new RefundModel();
        target.setRefundId(HasherUtil.hash(source.getRefundId()));
        target.setOrderId(HasherUtil.hash(source.getOrderId()));
        target.setAmount(source.getAmount());
        target.setReason(source.getReason());
        target.setCreatedAt(source.getCreatedAt() == null ? null : source.getCreatedAt().toString());
        return target;
    }
}
