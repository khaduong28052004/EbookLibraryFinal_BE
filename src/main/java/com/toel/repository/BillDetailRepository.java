package com.toel.repository;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.toel.model.BillDetail;

public interface BillDetailRepository extends JpaRepository<BillDetail, Integer> {
        @Query("SELECT COALESCE(AVG((bd.price - bd.discountPrice) * bd.quantity), 0) " +
                        "FROM BillDetail bd " +
                        "WHERE bd.product.account.id = :accountId " +
                        "AND (:dateStart IS NULL OR bd.bill.finishAt >= :dateStart) " +
                        "AND (:dateEnd IS NULL OR bd.bill.finishAt <= :dateEnd)")
        Double calculateAverageBillByShop(@Param("accountId") Integer accountId,
                        @Param("dateStart") LocalDate dateStart,
                        @Param("dateEnd") LocalDate dateEnd);

        @Query("SELECT COALESCE(AVG((bd.price * bd.quantity) * (bd.bill.discountRate.discount / 100)),0) " +
                        "FROM BillDetail bd " +
                        "WHERE bd.product.account.id = :accountId " +
                        "AND (:dateStart IS NULL OR bd.bill.finishAt >= :dateStart) " +
                        "AND (:dateEnd IS NULL OR bd.bill.finishAt <= :dateEnd)")
        Double calculateChietKhauByShop_San(@Param("accountId") Integer accountId,
                        @Param("dateStart") LocalDate dateStart,
                        @Param("dateEnd") LocalDate dateEnd);

}
