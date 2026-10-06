package com.example.mock.order.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Refund {
    private Long refundId;
    private Long orderId;
    private BigDecimal amount;
    private String reason;
    private LocalDateTime createdAt;
}
