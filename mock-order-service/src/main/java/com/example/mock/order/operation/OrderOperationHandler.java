package com.example.mock.order.operation;

import com.example.mock.order.entity.Order;
import com.example.mock.order.entity.OrderCriteria;
import com.example.mock.order.service.OrderService;
import com.example.mock.order.translator.OrderTranslator;
import com.example.mock.order.util.ResponseUtil;
import com.example.mock.order.view.swagger.model.OrderCancelData;
import com.example.mock.order.view.swagger.model.OrderRequestBody;
import com.example.mock.order.view.swagger.model.OrderResponseBody;
import com.example.mock.order.common.util.HasherUtil;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class OrderOperationHandler extends GenericOperationHandler<OrderRequestBody, OrderResponseBody> {

    @Autowired
    private OrderService orderService;

    @Override
    public ResponseEntity<OrderResponseBody> executeGetAction(OrderRequestBody requestData) {
        Preconditions.checkNotNull(requestData.getGetData(), GET_DATA_REQUIRED_MSG);
        OrderCriteria criteria = OrderTranslator.toCriteria(requestData.getGetData());

        List<Order> orders = orderService.findByCriteria(criteria);
        return ResponseUtil.createSuccessResponse(orders, OrderTranslator::toViewModel, OrderResponseBody.class);
    }

    @Override
    public ResponseEntity<OrderResponseBody> executeAddAction(OrderRequestBody requestData) {
        Preconditions.checkNotNull(requestData.getAddData(), ADD_DATA_REQUIRED_MSG);
        Long customerId = HasherUtil.unhash(requestData.getAddData().getCustomerId());

        Order created = orderService.create(customerId, OrderTranslator.toItems(requestData.getAddData().getItems()));
        return ResponseUtil.createSuccessResponse(Collections.singletonList(created), OrderTranslator::toViewModel, OrderResponseBody.class);
    }

    @Override
    public ResponseEntity<OrderResponseBody> executeCancelAction(OrderRequestBody requestData) {
        OrderCancelData data = Preconditions.checkNotNull(requestData.getCancelData(), CANCEL_DATA_REQUIRED_MSG);
        Order cancelled = orderService.cancel(HasherUtil.unhash(data.getOrderId()), data.getReason());
        return ResponseUtil.createSuccessResponse(Collections.singletonList(cancelled), OrderTranslator::toViewModel, OrderResponseBody.class);
    }
}
