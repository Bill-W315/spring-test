package com.example.mock.order.service.impl;

import com.example.mock.order.entity.Order;
import com.example.mock.order.entity.OrderCriteria;
import com.example.mock.order.entity.OrderItem;
import com.example.mock.order.model.exception.AppException;
import com.example.mock.order.persistence.dao.OrderDao;
import com.example.mock.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Override
    public List<Order> findByCriteria(OrderCriteria criteria) {
        return orderDao.findByCriteria(criteria);
    }

    @Override
    public Order create(Long customerId, List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new AppException(40000, "items must not be empty");
        }
        Long orderId = orderDao.create(customerId, items);
        return findOne(orderId);
    }

    @Override
    public Order cancel(Long orderId, String reason) {
        orderDao.cancel(orderId, reason);
        // Read back from the replica: right after the write this can still show the old status (replication lag).
        return findOne(orderId);
    }

    private Order findOne(Long orderId) {
        OrderCriteria criteria = new OrderCriteria();
        criteria.setOrderId(orderId);
        return orderDao.findByCriteria(criteria).stream().findFirst().orElse(null);
    }
}
