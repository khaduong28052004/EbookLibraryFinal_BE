package com.toel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.OrderStatus;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {

}
