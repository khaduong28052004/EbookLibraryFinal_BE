package com.toel.service.admin;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.toel.dto.admin.request.DiscountRate.Request_DiscountRateCreate;
import com.toel.dto.admin.request.DiscountRate.Request_DiscountRateUpdate;
import com.toel.dto.admin.response.Response_DiscountRate;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.DiscountRateMapper;
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

    public PageImpl<Response_DiscountRate> getAll(int page, int size, LocalDateTime search, boolean sortBy,
            String sortColumn) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
        Page<DiscountRate> pageDiscount;
        if (search == null) {
            pageDiscount = discountRateRepository.findAll(pageable);
        } else {
            pageDiscount = discountRateRepository
                    .findAllByDateDeleteOrDateCreateOrDateStart(search, search, search, pageable);
        }
        List<Response_DiscountRate> list = pageDiscount.stream()
                .map(discount -> discountRateMapper.tochChietKhauResponse(discount))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pageDiscount.getTotalElements());
    }

    public Response_DiscountRate getById(Integer id) {
        DiscountRate discountRate = discountRateRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Chiết khấu"));
        return discountRateMapper.tochChietKhauResponse(discountRate);
    }

    public Response_DiscountRate update(Request_DiscountRateUpdate discountRateUpdate) {
        DiscountRate discountRate = discountRateRepository.findById(discountRateUpdate.getId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Chiết khấu"));
        return Optional.of(discountRate)
                .map(entity -> {
                    entity.setDateStart(entity.getDateStart());
                    entity.setDiscount(entity.getDiscount());
                    return entity;
                })
                .filter(this::check)
                .map(discountRateRepository::saveAndFlush)
                .map(discountRateMapper::tochChietKhauResponse)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_SETUP, "Ngày áp dụng đã tồn tại"));

    }

    public Response_DiscountRate create(Request_DiscountRateCreate discountRate) {
        return Optional.of(discountRate)
                .map(discountRateMapper::toDiscountRateCreate)
                .map(entity -> {
                    entity.setAccount(accountRepository.findById(1)
                            .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND,
                                    "Account")));
                    return entity;
                })
                .filter(this::check)
                .map(discountRateRepository::saveAndFlush)
                .map(discountRateMapper::tochChietKhauResponse)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_SETUP, "Ngày áp dụng đã tồn tại"));

    }

    public void delete(Integer id) {
        DiscountRate discountRate = discountRateRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Chiết khấu"));
        if (discountRate.getDateStart().isAfter(LocalDateTime.now())) {
            discountRateRepository.delete(discountRate);
        } else {
            throw new AppException(ErrorCode.OBJECT_ACTIVE, "Chiết khấu");
        }
    }

    @Scheduled(fixedDelay = 60000)
    // @Scheduled(fixedDelay = 100)
    public void run() {
        if (discountRateRepository.findAllBydateDeleteIsNull().size() >= 2) {
            DiscountRate discountRate = discountRateRepository.findLatestDiscountRate().get(0);
            discountRateRepository.findAllBydateDeleteIsNull().forEach(rate -> {
                if (rate.getDateStart().isBefore(LocalDateTime.now()) && rate.getId() != discountRate.getId())
                    rate.setDateDelete(LocalDateTime.now());
                discountRateRepository.save(rate);
            });
        }
    }

    public boolean check(DiscountRate discountRate) {
        return discountRateRepository.findAllBydateDeleteIsNull().stream()
                .noneMatch(entity -> discountRate.getDateStart() == entity.getDateStart()
                        && (discountRate.getId() == null || discountRate.getId() != entity.getId()));
    }
}
