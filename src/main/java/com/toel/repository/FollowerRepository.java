package com.toel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.Follower;



public interface FollowerRepository extends JpaRepository<Follower, Integer> {


}