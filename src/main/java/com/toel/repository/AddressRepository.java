package com.toel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {

}
