package com.toel.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.Account;
import com.toel.model.TypeVoucher;
import com.toel.model.Voucher;

public interface VoucherRepository extends JpaRepository<Voucher, Integer> {

	@Query("SELECT v FROM Voucher v WHERE v.account.id = ?1 " + "AND (?2 IS NULL OR v.name LIKE CONCAT('%', ?2, '%'))")
	Page<Voucher> findAllByIdAccountSearch(Integer idAccount, String search, Pageable pageable);

	@Query("SELECT v FROM Voucher v WHERE v.typeVoucher.id = 2 "
			+ "AND (?1 IS NULL OR v.name LIKE CONCAT('%', ?1, '%'))")
	Page<Voucher> findAllByIdAccountSearch(String search, Pageable pageable);

	@Query("SELECT v FROM Voucher v WHERE v.account.id = ?1 ")
	Page<Voucher> findAllByIdAccount(Integer idAccount, Pageable pageable);

	@Query("SELECT v FROM Voucher v WHERE LOWER(v.account.role.name) LIKE 'adminv1' OR LOWER(v.account.role.name) LIKE 'admin' AND (:key IS NULL OR v.name = :key) ")
	Page<Voucher> findAllByAdmin(@Param("key") String key, Pageable pageable);

	@Query("SELECT v FROM Voucher v WHERE v.account.id = ?1 ")
	List<Voucher> findAllListByIdAccount(Integer idAccount);

	List<Voucher> findAllByAccount(Account account);

	@Query("SELECT v FROM Voucher v WHERE (v.typeVoucher = ?1) AND (?2 BETWEEN v.dateStart AND v.dateEnd) AND (isDelete = false)")
	List<Voucher> findAllByTypeVoucher(TypeVoucher typeVoucher, Date now);

}
