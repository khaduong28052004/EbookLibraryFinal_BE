package com.toel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toel.model.VoucherDetail;
import com.toel.model.Voucher;

public interface VoucherDetailRepository extends JpaRepository<VoucherDetail, Integer> {
    Page<VoucherDetail> findAllByVoucher(Voucher voucher, Pageable pageable);

    @Query("SELECT vd FROM VoucherDetail vd WHERE vd.voucher.account.id = ?1")
    Page<VoucherDetail> findAllByVoucherId(Integer voucher_id, Pageable pageable);
}
