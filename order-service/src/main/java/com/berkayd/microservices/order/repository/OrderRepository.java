package com.berkayd.microservices.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.berkayd.microservices.order.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
