package com.toel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.Product;



public interface ProductRepository extends JpaRepository<Product, Integer> {


}
