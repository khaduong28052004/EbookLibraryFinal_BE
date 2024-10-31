package com.toel.service.admin;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.toel.dto.admin.request.DiscountRate.Request_DiscountRateCreate;
import com.toel.dto.admin.response.Response_DiscountRate;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.DiscountRateMapper;
import com.toel.model.Account;
import com.toel.model.DiscountRate;
import com.toel.repository.AccountRepository;
import com.toel.repository.DiscountRateRepository;

@Service
public class Service_DiscountRate {
    @Autowired
    DiscountRateRepository discountRateRepository;
    @Autowired
    DiscountRateMapper discountRateMapper;
    @Autowired
    AccountRepository accountRepository;

    public PageImpl<Response_DiscountRate> getAll(int page, int size, LocalDateTime search,boolean sortBy, String sortColumn) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
        Page<DiscountRate> pageDiscount;
        if (search == null) {
            pageDiscount = discountRateRepository.findAll(pageable);
        } else {
            pageDiscount = discountRateRepository
                    .findAllByDateDeleteOrDateCreateOrDateStart(search, search, search,pageable);
        }
        List<Response_DiscountRate> list = pageDiscount.stream()
                .map(discount -> discountRateMapper.tochChietKhauResponse(discount))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pageDiscount.getTotalElements());
    }

    public Response_DiscountRate create(Request_DiscountRateCreate discountRate) {
        Account account = accountRepository.findById(discountRate.getAccount())
        .orElseThrow(()-> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Account"));
        DiscountRate entity = discountRateMapper.toDiscountRateCreate(discountRate);
        entity.setAccount(account);
        entity.setDateInsert(LocalDateTime.now());
        entity.setDateStart(discountRate.getDateStart());
        discountRateRepository.findAllBydateDeleteIsNull().forEach(rate -> {
            rate.setDateDelete(LocalDateTime.now());
            discountRateRepository.save(rate);
        });
        return discountRateMapper.tochChietKhauResponse(discountRateRepository.save(entity));
    }
}
