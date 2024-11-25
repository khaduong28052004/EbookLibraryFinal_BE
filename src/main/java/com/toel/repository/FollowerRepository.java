package com.toel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.Account;
import com.toel.model.Follower;
import java.util.List;

public interface FollowerRepository extends JpaRepository<Follower, Integer> {
	List<Follower> findAllByShopId(int shopId);

	Follower findByAccountAndShopId(Account user, Integer id_shop);

}