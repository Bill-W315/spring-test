package com.example.mock.order.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItem {
    private String sku;
    private Integer quantity;
    private BigDecimal unitPrice;
}
