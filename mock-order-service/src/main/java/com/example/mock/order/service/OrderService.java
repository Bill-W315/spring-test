package com.example.mock.order.service;

import com.example.mock.order.entity.Order;
import com.example.mock.order.entity.OrderCriteria;
import com.example.mock.order.entity.OrderItem;

import java.util.List;

public interface OrderService {

    List<Order> findByCriteria(OrderCriteria criteria);

    Order create(Long customerId, List<OrderItem> items);

    Order cancel(Long orderId, String reason);
}
