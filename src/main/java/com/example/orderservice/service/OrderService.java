package com.example.orderservice.service;

import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderStatus;
import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {

	private final OrderRepository orderRepository;

	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public Order create(CreateOrderRequest request) {
		Order order = new Order();
		order.setCustomerId(request.customerId());
		order.setProductSku(request.productSku());
		order.setQuantity(request.quantity());
		order.setTotalAmount(request.totalAmount());
		order.setStatus(OrderStatus.CREATED);
		return orderRepository.save(order);
	}

	@Cacheable(value = "orders", key = "#id")
	public Order getById(Long id) {
		return orderRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Order not found: " + id));
	}

	public List<Order> getByCustomer(String customerId) {
		return orderRepository.findByCustomerId(customerId);
	}

	@CacheEvict(value = "orders", key = "#id")
	public Order updateStatus(Long id, OrderStatus status) {
		Order order = getById(id);
		order.setStatus(status);
		return orderRepository.save(order);
	}
}
