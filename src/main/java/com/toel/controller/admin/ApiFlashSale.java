package com.toel.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.admin.request.FlashSale.Request_FlashSaleCreate;
import com.toel.dto.admin.request.FlashSale.Request_FlashSaleUpdate;
import com.toel.dto.admin.response.Response_FlashSale;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.model.FlashSale;
import com.toel.repository.FlashSaleRepository;
import com.toel.service.admin.Service_FlashSale;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/flashsale")
public class ApiFlashSale {
    @Autowired
    Service_FlashSale service_FlashSale;
    @Autowired
    FlashSaleRepository flashSaleRepository;

    @GetMapping
    public ApiResponse<PageImpl<Response_FlashSale>> getAll(
            @RequestParam(value = "dateStart", required = false) LocalDate dateStart,
            @RequestParam(value = "dateEnd", required = false) LocalDate dateEnd,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
            @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
        return ApiResponse.<PageImpl<Response_FlashSale>>build()
                .result(service_FlashSale.getAll(page, size, sortBy, sortColumn, dateStart, dateEnd));
    }

    @GetMapping("id")
    public ApiResponse<Response_FlashSale> getById(@RequestParam(value = "id", required = false) Integer id) {
        return ApiResponse.<Response_FlashSale>build()
                .result(service_FlashSale.getId(id));
    }

    @PostMapping
    public ApiResponse<Response_FlashSale> post(@RequestBody @Valid Request_FlashSaleCreate entity) {
        check(entity.getDateStart(), entity.getDateEnd(), null);
        return ApiResponse.<Response_FlashSale>build()
                .message("Thêm flash sale thành công")
                .result(service_FlashSale.create(entity));
    }

    @PutMapping
    public ApiResponse<Response_FlashSale> put(@RequestBody @Valid Request_FlashSaleUpdate entity) {
        check(entity.getDateStart(), entity.getDateEnd(), entity.getId());
        return ApiResponse.<Response_FlashSale>build()
                .message("Cập nhật flash sale thành công")
                .result(service_FlashSale.update(entity));
    }

    @DeleteMapping
    public ApiResponse<Response_FlashSale> delete(
            @RequestParam(value = "id", required = false) Integer id) {
        service_FlashSale.delete(id);
        return ApiResponse.<Response_FlashSale>build()
                .message("Xóa flashsale thành công");
    }

    private void check(LocalDateTime dateStart, LocalDateTime dateEnd, Integer id) {
        List<FlashSale> list = flashSaleRepository.findByIsDelete(false);
        for (FlashSale entity : list) {
            boolean isDateInRange = dateStart.isAfter(entity.getDateStart()) && dateStart.isBefore(entity.getDateEnd());
            boolean isIdMatching = id == null || entity.getId().equals(id);
            if (isDateInRange && !isIdMatching) {
                throw new AppException(ErrorCode.OBJECT_ALREADY_EXISTS, "Khung thời gian");
            }
        }
        long hours = Duration.between(dateStart, dateEnd).toHours();
        if (hours > 24) {
            throw new AppException(ErrorCode.TIME_ERROR);
        }
    }
}
