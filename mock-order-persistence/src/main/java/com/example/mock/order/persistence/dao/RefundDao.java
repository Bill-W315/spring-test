package com.example.mock.order.persistence.dao;

import com.example.mock.order.entity.Refund;

import java.math.BigDecimal;
import java.util.List;

public interface RefundDao {

    List<Refund> findByOrderId(Long orderId);

    void create(Long orderId, BigDecimal amount, String reason);
}
