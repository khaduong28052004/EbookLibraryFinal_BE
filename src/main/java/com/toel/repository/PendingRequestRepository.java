package com.toel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.PendingRequest;

public interface PendingRequestRepository extends JpaRepository<PendingRequest, Integer> {

}
