package com.toel.service.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.Scheduled;
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
import com.toel.util.log.LogUtil;

@Service
public class Service_FlashSale {
        @Autowired
        FlashSaleRepository flashSaleRepository;
        @Autowired
        FlashSaleMapper flashSaleMapper;
        @Autowired
        AccountRepository accountRepository;
        @Autowired
        LogUtil service_Log;

        public PageImpl<Response_FlashSale> getAll(int page, int size, Boolean sortBy, String column,
                        LocalDate dateStart, LocalDate dateEnd) {
                Pageable pageable = PageRequest.of(page, size,
                                Sort.by(sortBy ? Direction.DESC : Direction.ASC, column));
                Page<FlashSale> pageFlashSale;
                if (dateStart == null || dateEnd == null) {
                        pageFlashSale = flashSaleRepository.findAll(pageable);
                } else {
                        pageFlashSale = flashSaleRepository.findAllByDateStartOrDateEnd(dateStart, dateEnd,
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

        public Response_FlashSale create(Request_FlashSaleCreate flashSaleCreate, Integer accountID) {
                Account account = accountRepository.findById(flashSaleCreate.getAccount())
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Account"));
                FlashSale entity = flashSaleMapper.toFlashSaleCreate(flashSaleCreate);
                entity.setAccount(account);
                FlashSale flashSaleNew = flashSaleRepository.save(entity);
                service_Log.setLog(getClass(), accountID, "INFO", "FlashSale",
                                flashSaleMapper.tResponse_FlashSale(flashSaleNew), null, "Tạo Flash sale");
                return flashSaleMapper
                                .tResponse_FlashSale(flashSaleNew);
        }

        public Response_FlashSale update(Request_FlashSaleUpdate flashSaleUpdate, Integer accountID) {
                FlashSale entity = flashSaleRepository.findById(flashSaleUpdate.getId())
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "FlashSale"));
                FlashSale entityOld = flashSaleRepository.findById(flashSaleUpdate.getId())
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "FlashSale"));
                Account account = accountRepository.findById(flashSaleUpdate.getAccount())
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Account"));
                flashSaleMapper.toFlashSaleUpdate(entity, flashSaleUpdate);
                entity.setAccount(account);
                FlashSale flashSaleNew = flashSaleRepository.save(entity);
                service_Log.setLog(getClass(), accountID, "INFO", "FlashSale",
                                flashSaleMapper.tResponse_FlashSale(entityOld),
                                flashSaleMapper.tResponse_FlashSale(flashSaleNew),
                                "Cập nhật Flash sale");
                return flashSaleMapper.tResponse_FlashSale(flashSaleNew);
        }

        public void delete(Integer id, Integer accountID) {
                FlashSale entity = flashSaleRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "FlashSale"));
                if (entity.getDateStart().isBefore(LocalDateTime.now())
                                && entity.getDateEnd().isAfter(LocalDateTime.now())) {
                        // service_Log.setLog(getClass(), accountID, "ERROR", "FLASHSALE", entity,
                        // "Xóa Flash sale");
                        throw new AppException(ErrorCode.OBJECT_ACTIVE, "Flash Sale");
                }
                entity.setDelete(true);
                FlashSale flashSaleNew = flashSaleRepository.save(entity);
                service_Log.setLog(getClass(), accountID, "INFO", "FlashSale",
                                flashSaleMapper.tResponse_FlashSale(flashSaleNew), null,
                                "Xóa Flash sale");
        }

        @Scheduled(fixedDelay = 60000)
        public void run() {
                if (flashSaleRepository.findByIsDelete(false).size() >= 1) {
                        flashSaleRepository.findByIsDelete(false).forEach(flasesale -> {
                                if (flasesale.getDateEnd().isBefore(LocalDateTime.now())) {
                                        flasesale.setDelete(true);
                                        flashSaleRepository.save(flasesale);
                                }
                        });
                }
        }

}
