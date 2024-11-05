package com.toel.service.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.toel.dto.admin.response.Response_VoucherDetatils;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.VoucherDetailsMapper;
import com.toel.model.Voucher;
import com.toel.model.VoucherDetail;
import com.toel.repository.VoucherDetailRepository;
import com.toel.repository.VoucherRepository;

@Service
public class Service_VoucherDetails {
    @Autowired
    VoucherDetailRepository voucherDetailRepository;
    @Autowired
    VoucherRepository voucherRepository;
    @Autowired
    VoucherDetailsMapper voucherDetailsMapper;

    public PageImpl<Response_VoucherDetatils> getAllByVoucher(Integer voucherId,
            Integer page, Integer size, boolean sortBy, String sortColumn) {
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Voucher"));
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
        Page<VoucherDetail> pageVoucherDetails = voucherDetailRepository.findAllByVoucher(voucher, pageable);
        List<Response_VoucherDetatils> list = pageVoucherDetails.stream()
                .map(voucherDetail -> voucherDetailsMapper.toResponse_VoucherDetatils(voucherDetail))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pageVoucherDetails.getTotalElements());
    }

    public Response_VoucherDetatils getById(int id) {
        VoucherDetail voucherDetail = voucherDetailRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "VoucherDetail"));
        return voucherDetailsMapper.toResponse_VoucherDetatils(voucherDetail);
    }
}
