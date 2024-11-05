package com.toel.service.admin;

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

import com.toel.dto.admin.request.voucher.Request_VoucherCreate;
import com.toel.dto.admin.request.voucher.Request_VoucherUpdate;
import com.toel.dto.admin.response.Response_Voucher;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.VoucherMapper;
import com.toel.model.Account;
import com.toel.model.TypeVoucher;
import com.toel.model.Voucher;
import com.toel.repository.AccountRepository;
import com.toel.repository.TypeVoucherRepository;
import com.toel.repository.VoucherRepository;

@Service
public class Service_Voucher {
    @Autowired
    VoucherRepository voucherRepository;
    @Autowired
    VoucherMapper voucherMapper;
    @Autowired
    TypeVoucherRepository typeVoucherRepository;
    @Autowired
    AccountRepository accountRepository;

    public PageImpl<Response_Voucher> getAllAdmin(String key,
            Integer page, Integer size, boolean sortBy, String sortColumn) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
        Page<Voucher> pageVoucher = voucherRepository.findAllByAdmin(key, pageable);
        List<Response_Voucher> list = pageVoucher.stream()
                .map(voucher -> voucherMapper.toAdmiResponse_Voucher(voucher))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pageVoucher.getTotalElements());
    }

    public PageImpl<Response_Voucher> getAllByAccount(
            Integer page, Integer size, boolean sortBy, String sortColumn, Integer account_id) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
        Account account = accountRepository.findById(account_id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Account"));
        Page<Voucher> pageVoucher = voucherRepository.findAllByIdAccount(account.getId(),
                pageable);
        List<Response_Voucher> list = pageVoucher.stream()
                .map(voucher -> voucherMapper.toAdmiResponse_Voucher(voucher))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pageVoucher.getTotalElements());
    }

    public Response_Voucher edit(Integer voucher_id) {
        return voucherMapper.toAdmiResponse_Voucher(voucherRepository.findById(voucher_id).get());
    }

    public Response_Voucher update(Request_VoucherUpdate voucherUpdate) {
        Voucher voucher = voucherRepository.findById(voucherUpdate.getId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Voucher"));
        voucherMapper.toAdmin_voucherUpdate(voucher, voucherUpdate);
        TypeVoucher typeVoucher = typeVoucherRepository.findById(voucherUpdate.getTypeVoucher())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "TypeVoucher"));
        Account account = accountRepository.findById(voucherUpdate.getAccount())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Account"));
        voucher.setTypeVoucher(typeVoucher);
        voucher.setAccount(account);
        return voucherMapper.toAdmiResponse_Voucher(voucherRepository.save(voucher));

    }

    public Response_Voucher save(Request_VoucherCreate request_Voucher) {
        return voucherMapper.toAdmiResponse_Voucher(
                voucherRepository.saveAndFlush(voucherMapper.toAdmin_voucherCreate(request_Voucher)));
    }

    public boolean delete(Integer voucher_id) {
        Optional<Voucher> voucher = voucherRepository.findById(voucher_id);
        voucher.get().setDelete(!voucher.get().isDelete());
        voucherRepository.saveAndFlush(voucher.get());
        return voucher.get().isDelete();
    }
}
