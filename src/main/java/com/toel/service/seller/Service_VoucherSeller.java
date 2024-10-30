package com.toel.service.seller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.toel.dto.seller.request.Request_Voucher;
import com.toel.dto.seller.response.Response_Voucher;
import com.toel.mapper.seller.Seller_VoucherMapper;
import com.toel.model.Voucher;
import com.toel.repository.AccountRepository;
import com.toel.repository.TypeVoucherRepository;
import com.toel.repository.VoucherRepository;

@Service
public class Service_VoucherSeller {
    @Autowired
    VoucherRepository voucherRepository;
    @Autowired
    Seller_VoucherMapper voucherMapper;
    @Autowired
    TypeVoucherRepository typeVoucherRepository;
    @Autowired
    AccountRepository accountRepository;

    public PageImpl<Response_Voucher> getAll(
            Integer page, Integer size, boolean sortBy, String sortColumn, Integer account_id) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
        Page<Voucher> pageVoucher = voucherRepository.findAllByIdAccount(account_id,
                pageable);
        List<Response_Voucher> list = pageVoucher.stream()
                .map(voucher -> voucherMapper.response_Voucher(voucher))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pageVoucher.getTotalElements());
    }

    public Response_Voucher edit(Integer voucher_id) {
        return voucherMapper.response_Voucher(voucherRepository.findById(voucher_id).get());
    }

    public Response_Voucher save(Request_Voucher request_Voucher) {
        return voucherMapper.response_Voucher(voucherRepository.saveAndFlush(voucherMapper.voucher(request_Voucher)));
    }

    public boolean delete(Integer voucher_id) {
        Optional<Voucher> voucher = voucherRepository.findById(voucher_id);
        voucher.get().setDelete(!voucher.get().isDelete());
        voucherRepository.saveAndFlush(voucher.get());
        return voucher.get().isDelete();
    }
}
