package com.example.mock.order.service;

import com.example.mock.order.entity.Refund;

import java.math.BigDecimal;
import java.util.List;

public interface RefundService {

    List<Refund> findByOrderId(Long orderId);

    /** Creates a refund and returns all refunds of the order. */
    List<Refund> refund(Long orderId, BigDecimal amount, String reason);
}
