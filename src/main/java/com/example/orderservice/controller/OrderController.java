package com.example.orderservice.controller;

import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderStatus;
import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping
	public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
		Order order = orderService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse.from(order));
	}

	@GetMapping("/{id}")
	public OrderResponse getById(@PathVariable Long id) {
		return OrderResponse.from(orderService.getById(id));
	}

	@GetMapping
	public List<OrderResponse> getByCustomer(@RequestParam String customerId) {
		return orderService.getByCustomer(customerId).stream()
				.map(OrderResponse::from)
				.toList();
	}

	@PatchMapping("/{id}/status")
	public OrderResponse updateStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
		return OrderResponse.from(orderService.updateStatus(id, status));
	}
}
