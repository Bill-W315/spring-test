package com.example.mock.order.entity;

import com.example.mock.order.entity.enumeration.OrderStatus;
import lombok.Data;

@Data
public class OrderCriteria {
    private Long orderId;
    private OrderStatus status;
}
