package com.toel.service.seller;

import java.util.ArrayList;
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

import com.toel.dto.seller.request.Voucher.Request_VoucherCreate;
import com.toel.dto.seller.request.Voucher.Request_VoucherUpdate;
import com.toel.dto.seller.response.Response_Voucher;
import com.toel.dto.seller.response.Response_VoucherDetail;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.VoucherDetailMapper;
import com.toel.mapper.VoucherMapper;
import com.toel.model.Voucher;
import com.toel.model.VoucherDetail;
import com.toel.repository.AccountRepository;
import com.toel.repository.TypeVoucherRepository;
import com.toel.repository.VoucherDetailRepository;
import com.toel.repository.VoucherRepository;

@Service
public class Service_VoucherSeller {
        @Autowired
        VoucherRepository voucherRepository;
        @Autowired
        VoucherMapper voucherMapper;
        @Autowired
        TypeVoucherRepository typeVoucherRepository;
        @Autowired
        AccountRepository accountRepository;
        @Autowired
        VoucherDetailRepository voucherDetailRepository;
        @Autowired
        VoucherDetailMapper voucherDetailMapper;

        public PageImpl<Response_Voucher> getAll(
                        Integer page, Integer size, boolean sortBy, String sortColumn, Integer account_id,
                        String search) {
                Pageable pageable = PageRequest.of(page, size,
                                Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
                Page<Voucher> pageVoucher = voucherRepository.findAllByIdAccountSearch(account_id, search,
                                pageable);
                List<Response_Voucher> list = pageVoucher.stream()
                                .map(voucher -> voucherMapper.response_Voucher(voucher))
                                .collect(Collectors.toList());
                return new PageImpl<>(list, pageable, pageVoucher.getTotalElements());
        }

        public PageImpl<Response_Voucher> getAllAdmin(
                        Integer page, Integer size, boolean sortBy, String sortColumn, String search) {
                Pageable pageable = PageRequest.of(page, size,
                                Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
                Page<Voucher> pageVoucher = voucherRepository.findAllByIdAccountSearch(search,
                                pageable);
                List<Response_Voucher> list = pageVoucher.stream()
                                .map(voucher -> voucherMapper.response_Voucher(voucher))
                                .collect(Collectors.toList());
                return new PageImpl<>(list, pageable, pageVoucher.getTotalElements());
        }

        public Response_Voucher edit(Integer voucher_id) {
                return voucherMapper.response_Voucher(voucherRepository.findById(voucher_id).get());
        }

        public Response_Voucher create(Request_VoucherCreate request_Voucher) {
                Voucher voucher = voucherMapper.voucherCreate(request_Voucher);
                // checkVoucher(voucher);
                voucher.setAccount(accountRepository.findById(request_Voucher.getAccount())
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Account")));
                voucher.setTypeVoucher(typeVoucherRepository.findById(request_Voucher.getTypeVoucher())
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "TypeVoucher")));
                return voucherMapper
                                .response_Voucher(voucherRepository.saveAndFlush(voucher));
        }

        public Response_Voucher update(Request_VoucherUpdate request_Voucher) {
                Voucher voucher = voucherMapper.voucherUpdate(request_Voucher);
                // checkVoucher(voucher);
                voucher.setAccount(accountRepository.findById(request_Voucher.getAccount())
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Account")));
                voucher.setTypeVoucher(typeVoucherRepository.findById(request_Voucher.getTypeVoucher())
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "TypeVoucher")));
                return voucherMapper
                                .response_Voucher(voucherRepository.saveAndFlush(voucher));
        }

        public boolean delete(Integer voucher_id) {
                Optional<Voucher> voucher = voucherRepository.findById(voucher_id);
                voucher.get().setDelete(!voucher.get().isDelete());
                voucherRepository.saveAndFlush(voucher.get());
                return voucher.get().isDelete();
        }

        public PageImpl<Response_VoucherDetail> getAllDetail(
                        Integer page, Integer size, boolean sortBy, String sortColum, Integer voucher_id,
                        String search) {
                Pageable pageable = PageRequest.of(page, size,
                                Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColum));
                Page<VoucherDetail> pageVoucherDetail = voucherDetailRepository.findAllByVoucherId(voucher_id, search,
                                pageable);
                List<Response_VoucherDetail> list = pageVoucherDetail.stream()
                                .map(voucherDetail -> voucherDetailMapper.response_VoucherDetail(voucherDetail))
                                .collect(Collectors.toList());
                return new PageImpl<>(list.isEmpty() ? new ArrayList<>() : list, pageable,
                                pageVoucherDetail.getTotalElements());
        }

        public Request_VoucherCreate checkVoucherCreate(Request_VoucherCreate request_VoucherCreate) {
                boolean nameExists = voucherRepository.findAllListByIdAccount(request_VoucherCreate.getAccount())
                                .stream()
                                .anyMatch(voucherCheck -> request_VoucherCreate.getName()
                                                .equalsIgnoreCase(voucherCheck.getName()));
                if (nameExists) {
                        throw new AppException(ErrorCode.OBJECT_SETUP, "Tên voucher đã tồn tại");
                }
                if (request_VoucherCreate.getSale() < 1 || request_VoucherCreate.getSale() > 100) {
                        throw new AppException(ErrorCode.OBJECT_SETUP, "Sale phải từ 1 - 100");
                }
                if (request_VoucherCreate.getDateStart().after(request_VoucherCreate.getDateEnd())) {
                        throw new AppException(ErrorCode.OBJECT_SETUP, "Ngày bắt đầu không thể lớn hơn ngày kết thúc");
                }
                return request_VoucherCreate;
        }

        public Request_VoucherUpdate checkVoucherUpdate(Request_VoucherUpdate request_VoucherUpdate) {
                boolean nameExists = voucherRepository.findAllListByIdAccount(request_VoucherUpdate.getAccount())
                                .stream()
                                .anyMatch(voucherCheck -> request_VoucherUpdate.getName()
                                                .equalsIgnoreCase(voucherCheck.getName())
                                                && !request_VoucherUpdate
                                                                .getId().equals(voucherCheck.getId()));
                if (nameExists) {
                        throw new AppException(ErrorCode.OBJECT_SETUP, "Tên voucher đã tồn tại");
                }

                if (request_VoucherUpdate.getSale() < 1 || request_VoucherUpdate.getSale() > 100) {
                        throw new AppException(ErrorCode.OBJECT_SETUP, "Sale phải từ 1 - 100");
                }
                if (request_VoucherUpdate.getDateStart().after(request_VoucherUpdate.getDateEnd())) {
                        throw new AppException(ErrorCode.OBJECT_SETUP, "Ngày bắt đầu không thể lớn hơn ngày kết thúc");
                }
                return request_VoucherUpdate;
        }

}
