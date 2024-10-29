package com.toel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.Address;
import com.toel.model.BillDetail;

public interface AddressRepository extends JpaRepository<Address, Integer>{
}

