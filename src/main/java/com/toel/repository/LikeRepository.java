package com.toel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toel.model.Like;

public interface LikeRepository extends JpaRepository<Like, Integer> {

    @Query("Select COUNT(l) FROM Like l WHERE l.product.account.id = ?1")
    Integer getLike(Integer account_id);

}
