package com.toel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {

}
