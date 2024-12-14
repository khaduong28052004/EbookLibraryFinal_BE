package com.toel.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.toel.model.UserProductActions;
import java.util.List;

@Repository
public interface UserProductActionsRepository extends JpaRepository<UserProductActions, Long> {

    // Tìm bản ghi dựa trên userId và productId
    Optional<UserProductActions> findByUserIdAndProductId(Integer userId, Integer productId);

    List<UserProductActions> findByUserId(Integer userId);

    List<UserProductActions> findByLastActionTime(LocalDateTime lastActionTime);

    List<UserProductActions> findByLastActionTimeBetween(LocalDateTime start, LocalDateTime end);

    List<UserProductActions> findByUserIdAndLastActionTime(Integer userId, LocalDateTime lastActionTime);

    List<UserProductActions> findByUserIdAndLastActionTimeBetween(Integer userId, LocalDateTime start, LocalDateTime end);

    
    
    // Cập nhật số lượng cho hành động cụ thể
    // @Modifying
    // @Query("UPDATE UserProductActions u SET u.viewCount = u.viewCount + :count,
    // u.lastActionTime = :time WHERE u.userId = :userId AND u.productId =
    // :productId")
    // void incrementViewCount(@Param("userId") Long userId,
    // @Param("productId") Long productId,
    // @Param("count") int count,
    // @Param("time") LocalDateTime time);

    // Tương tự cho `add_to_cart` và `purchase`.
}
