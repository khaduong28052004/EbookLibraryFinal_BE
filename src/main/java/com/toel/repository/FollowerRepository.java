package com.toel.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.Follower;

import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

public interface FollowerRepository extends JpaRepository<Follower, Integer> {
    List<Follower> findAllByShopId(int shopId);

    @Query("SELECT COUNT(f) FROM Follower f WHERE f.shopId.id = :shopId")
    Integer countFollowersByShopId(@Param("shopId") Integer shopId);

    @Query("SELECT COUNT(f) FROM Follower f WHERE f.accountId.id = :accountId")
    Integer countFollowingByAccountId(@Param("accountId") Integer accountId);
    
    // @Query("SELECT COUNT(f) FROM Product f WHERE f.account.id = :accountId")
    //     Integer countProductByAccountId(@Param("accountId") Integer accountId);
    @Query("SELECT COUNT(f) FROM Product f WHERE f.account.id = :accountId")
    Integer countProductByAccountId(@Param("accountId") Integer accountId);
}