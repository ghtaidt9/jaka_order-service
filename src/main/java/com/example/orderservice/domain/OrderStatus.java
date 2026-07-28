package com.example.orderservice.domain;

public enum OrderStatus {
	CREATED,
	PENDING_PAYMENT,
	PAID,
	CANCELLED,
	FAILED
}
