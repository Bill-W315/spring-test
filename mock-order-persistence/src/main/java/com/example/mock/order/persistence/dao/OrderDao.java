package com.example.mock.order.persistence.dao;

import com.example.mock.order.entity.Order;
import com.example.mock.order.entity.OrderCriteria;
import com.example.mock.order.entity.OrderItem;

import java.util.List;

public interface OrderDao {

    List<Order> findByCriteria(OrderCriteria criteria);

    Long create(Long customerId, List<OrderItem> items);

    void cancel(Long orderId, String reason);
}
