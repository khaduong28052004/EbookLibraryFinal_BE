package com.toel.service.admin;

import java.time.LocalDate;
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

import com.toel.dto.admin.request.FlashSale.Request_FlashSaleCreate;
import com.toel.dto.admin.request.FlashSale.Request_FlashSaleUpdate;
import com.toel.dto.admin.response.Response_FlashSale;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.FlashSaleMapper;
import com.toel.model.Account;
import com.toel.model.FlashSale;
import com.toel.repository.AccountRepository;
import com.toel.repository.FlashSaleRepository;

@Service
public class Service_FlashSale {
    @Autowired
    FlashSaleRepository flashSaleRepository;
    @Autowired
    FlashSaleMapper flashSaleMapper;
    @Autowired
    AccountRepository accountRepository;

    public PageImpl<Response_FlashSale> getAll(int page, int size, Boolean sortBy, String column,
            LocalDate dateStart, LocalDate dateEnd) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy ? Direction.DESC : Direction.ASC, column));
        Page<FlashSale> pageFlashSale;
        if (dateStart == null || dateEnd == null) {
            pageFlashSale = flashSaleRepository.findAllByIsDelete(false, pageable);
        } else {
            pageFlashSale = flashSaleRepository.findAllByDateStartOrDateEndAndIsDelete(dateStart, dateEnd, false,
                    pageable);
        }
        List<Response_FlashSale> list = pageFlashSale.stream()
                .map(flashsale -> flashSaleMapper.tResponse_FlashSale(flashsale))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pageFlashSale.getTotalElements());
    }

    public Response_FlashSale getId(Integer id) {
        FlashSale flashSale = flashSaleRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "FlashSale"));
        return flashSaleMapper.tResponse_FlashSale(flashSale);
    }

    public Response_FlashSale create(Request_FlashSaleCreate flashSaleCreate) {
        Account account = accountRepository.findById(flashSaleCreate.getAccount())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Account"));
        FlashSale entity = flashSaleMapper.toFlashSaleCreate(flashSaleCreate);
        entity.setAccount(account);
        return flashSaleMapper
                .tResponse_FlashSale(flashSaleRepository.save(entity));
    }

    public Response_FlashSale update(Request_FlashSaleUpdate flashSaleUpdate) {
        FlashSale entity = flashSaleRepository.findById(flashSaleUpdate.getId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "FlashSale"));
        Account account = accountRepository.findById(flashSaleUpdate.getAccount())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Account"));
        flashSaleMapper.toFlashSaleUpdate(entity, flashSaleUpdate);
        entity.setAccount(account);
        return flashSaleMapper.tResponse_FlashSale(flashSaleRepository.save(entity));
    }

    public void delete(Integer id) {
        FlashSale entity = flashSaleRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "FlashSale"));
        entity.setDelete(true);
        flashSaleRepository.save(entity);
    }
}
