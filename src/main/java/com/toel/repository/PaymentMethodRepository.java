package com.toel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.PaymentMethod;



public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {

}
