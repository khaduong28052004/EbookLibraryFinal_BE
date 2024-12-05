package com.toel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toel.model.Account;
import com.toel.model.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {

    List<Address> findAllByAccount(Account account);

    List<Address> findByAccount(Account account);

    List<Address> findByStatusAndAccount(Boolean status, Account account);

    @Query("SELECT a.fullNameAddress  FROM Address a  WHERE account.id = ?1")
    String findAddressByBill(Integer id);
}
