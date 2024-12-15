package com.toel.service.admin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.toel.dto.admin.request.DiscountRate.Request_DiscountRateCreate;
import com.toel.dto.admin.request.DiscountRate.Request_DiscountRateUpdate;
import com.toel.dto.admin.response.Response_DiscountRate;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.DiscountRateMapper;
import com.toel.model.DiscountRate;
import com.toel.model.Role;
import com.toel.repository.AccountRepository;
import com.toel.repository.DiscountRateRepository;
import com.toel.repository.RoleRepository;
import com.toel.service.Email.EmailService;
import com.toel.service.Email.EmailTemplateType;
import com.toel.util.log.LogUtil;

@Service
public class Service_DiscountRate {
    @Autowired
    DiscountRateRepository discountRateRepository;
    @Autowired
    DiscountRateMapper discountRateMapper;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    LogUtil service_Log;

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

    public Response_DiscountRate update(Request_DiscountRateUpdate discountRateUpdate, Integer accountID) {
        DiscountRate entity = discountRateRepository.findById(discountRateUpdate.getId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Chiết khấu"));
        Response_DiscountRate entityOld = discountRateMapper
                .tochChietKhauResponse(discountRateRepository.findById(discountRateUpdate.getId())
                        .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Chiết khấu")));
        List<DiscountRate> discountRateNow = discountRateRepository.findAllBydateDeleteIsNull();
        for (DiscountRate discountRate2 : discountRateNow) {
            if (discountRate2.getDiscount() == discountRateUpdate.getDiscount()
                    && discountRate2.getId() != discountRateUpdate.getId()) {
                throw new AppException(ErrorCode.OBJECT_ACTIVE, "Mức chiết khấu");
            }
        }
        entity.setDateStart(discountRateUpdate.getDateStart().atStartOfDay());
        entity.setDiscount(discountRateUpdate.getDiscount());
        if (check(entity)) {
            Response_DiscountRate dResponse_DiscountRate = discountRateMapper
                    .tochChietKhauResponse(discountRateRepository.save(entity));
            Role role = roleRepository.findById(3)
                    .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Role"));
            // Lấy danh sách email và tạo bản đồ email -> tên
            Map<String, String> emailToNameMap = new HashMap<>();
            List<String> listmail = new ArrayList<>();
            accountRepository.findAllByStatusAndRole(true, role).forEach(account -> {
                emailToNameMap.put(account.getEmail(), account.getFullname());
                listmail.add(account.getEmail());
            });
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = entity.getDateStart().format(formatter);
            emailService.pushList(
                    "TOEL - Thông Báo Cập Nhật Chiết Khấu",
                    listmail, // Gửi email cho từng người
                    EmailTemplateType.THEMCHIETKHAU,
                    emailToNameMap,
                    formattedDate,
                    entity.getDiscount().toString() + " %");
            service_Log.setLog(getClass(), accountID, "INFO", "DiscountRate", entityOld, dResponse_DiscountRate,
                    "Cập nhật chiết khấu");
            return dResponse_DiscountRate;
        } else {
            throw new AppException(ErrorCode.OBJECT_SETUP, "Ngày áp dụng đã tồn tại");
        }
    }

    public Response_DiscountRate create(Request_DiscountRateCreate discountRateCreate, Integer accountID) {
        DiscountRate entity = new DiscountRate();
        entity.setDiscount(discountRateCreate.getDiscount());
        entity.setDateStart(discountRateCreate.getDateStart().atStartOfDay());
        entity.setAccount(accountRepository.findById(1)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND,
                        "Account")));
        if (check(entity)) {
            List<DiscountRate> discountRateNow = discountRateRepository.findAllBydateDeleteIsNull();
            for (DiscountRate discountRate2 : discountRateNow) {
                if (discountRate2.getDiscount() == discountRateCreate.getDiscount()) {
                    throw new AppException(ErrorCode.OBJECT_ACTIVE, "Mức chiết khấu");
                }
            }
            entity.setDateInsert(LocalDateTime.now());
            DiscountRate discountRate = discountRateRepository.save(entity);
            Role role = roleRepository.findById(3)
                    .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Role"));
            // Lấy danh sách email và tạo bản đồ email -> tên
            Map<String, String> emailToNameMap = new HashMap<>();
            List<String> listmail = new ArrayList<>();
            accountRepository.findAllByStatusAndRole(true, role).forEach(account -> {
                emailToNameMap.put(account.getEmail(), account.getFullname());
                listmail.add(account.getEmail());
            });
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = entity.getDateStart().format(formatter);
            emailService.pushList(
                    "TOEL - Thông Báo Cập Nhật Chiết Khấu",
                    listmail, // Gửi email cho từng người
                    EmailTemplateType.THEMCHIETKHAU,
                    emailToNameMap,
                    formattedDate,
                    entity.getDiscount().toString() + " %");
            service_Log.setLog(getClass(), accountID, "INFO", "DiscountRate",
                    discountRateMapper.tochChietKhauResponse(discountRate), null,
                    "Thêm chiết khấu");
            return discountRateMapper.tochChietKhauResponse(discountRate);
        } else {
            throw new AppException(ErrorCode.OBJECT_SETUP, "Ngày áp dụng đã tồn tại");
        }
    }

    public void delete(Integer id, Integer accountID) {
        DiscountRate discountRate = discountRateRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Chiết khấu"));
        if (discountRate.getDateStart().isAfter(LocalDateTime.now())) {
            discountRateRepository.delete(discountRate);
            service_Log.setLog(getClass(), accountID, "INFO", "DiscountRate",
                    discountRateMapper.tochChietKhauResponse(discountRate), null,
                    "Xóa chiết khấu");
        } else {
            // service_Log.setLog(getClass(), accountID, "ERROR", "DiscountRate",
            // discountRateMapper.tochChietKhauResponse(discountRate),null,
            // "Xóa chiết khấu");
            throw new AppException(ErrorCode.OBJECT_ACTIVE, "Chiết khấu");
        }
    }

    public boolean check(DiscountRate discountRate) {
        return discountRateRepository.findAllBydateDeleteIsNull().stream()
                .noneMatch(entity -> discountRate.getDateStart() == entity.getDateStart()
                        && (discountRate.getId() == null || discountRate.getId() != entity.getId()));
    }
}
