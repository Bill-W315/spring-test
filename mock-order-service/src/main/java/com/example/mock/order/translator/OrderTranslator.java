package com.example.mock.order.translator;

import com.example.mock.order.entity.Order;
import com.example.mock.order.entity.OrderCriteria;
import com.example.mock.order.entity.OrderItem;
import com.example.mock.order.entity.enumeration.OrderStatus;
import com.example.mock.order.view.swagger.model.OrderGetData;
import com.example.mock.order.view.swagger.model.OrderItemData;
import com.example.mock.order.view.swagger.model.OrderModel;
import com.example.mock.order.common.util.HasherUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderTranslator {

    public static OrderCriteria toCriteria(OrderGetData source) {
        OrderCriteria target = new OrderCriteria();
        target.setOrderId(source.getOrderId() == null ? null : HasherUtil.unhash(source.getOrderId()));
        target.setStatus(source.getStatus() == null ? null : OrderStatus.valueOf(source.getStatus()));
        return target;
    }

    public static List<OrderItem> toItems(List<OrderItemData> source) {
        return source.stream().map(i -> {
            OrderItem item = new OrderItem();
            item.setSku(i.getSku());
            item.setQuantity(i.getQuantity());
            item.setUnitPrice(i.getUnitPrice());
            return item;
        }).collect(Collectors.toList());
    }

    public static OrderModel toViewModel(Order source) {
        OrderModel target = new OrderModel();
        target.setOrderId(HasherUtil.hash(source.getOrderId()));
        target.setCustomerId(HasherUtil.hash(source.getCustomerId()));
        target.setStatus(source.getStatus() == null ? null : source.getStatus().name());
        target.setTotalAmount(source.getTotalAmount());
        // The API promises 0 instead of null when nothing was refunded.
        target.setRefundAmount(Optional.ofNullable(source.getRefundAmount()).orElse(BigDecimal.ZERO));
        target.setCreatedAt(source.getCreatedAt() == null ? null : source.getCreatedAt().toString());
        return target;
    }
}
