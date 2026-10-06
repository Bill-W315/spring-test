package com.example.mock.order.service.impl;

import com.example.mock.order.entity.Refund;
import com.example.mock.order.model.exception.AppException;
import com.example.mock.order.persistence.dao.RefundDao;
import com.example.mock.order.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RefundServiceImpl implements RefundService {

    @Autowired
    private RefundDao refundDao;

    @Override
    public List<Refund> findByOrderId(Long orderId) {
        return refundDao.findByOrderId(orderId);
    }

    @Override
    public List<Refund> refund(Long orderId, BigDecimal amount, String reason) {
        if (amount == null || amount.signum() <= 0) {
            throw new AppException(40000, "amount must be positive");
        }
        refundDao.create(orderId, amount, reason);
        return refundDao.findByOrderId(orderId);
    }
}
