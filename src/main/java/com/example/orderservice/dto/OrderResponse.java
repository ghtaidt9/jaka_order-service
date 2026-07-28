package com.example.orderservice.dto;

import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderResponse(
		Long id,
		String customerId,
		String productSku,
		Integer quantity,
		BigDecimal totalAmount,
		OrderStatus status,
		Instant createdAt
) {
	public static OrderResponse from(Order order) {
		return new OrderResponse(
				order.getId(),
				order.getCustomerId(),
				order.getProductSku(),
				order.getQuantity(),
				order.getTotalAmount(),
				order.getStatus(),
				order.getCreatedAt()
		);
	}
}
