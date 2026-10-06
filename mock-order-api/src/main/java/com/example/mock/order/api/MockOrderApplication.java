package com.example.mock.order.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.mock.order")
public class MockOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(MockOrderApplication.class, args);
    }
}
