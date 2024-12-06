package com.toel.service.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.model.FlashSale;
import com.toel.model.FlashSaleDetail;
import com.toel.repository.AccountRepository;
import com.toel.repository.FlashSaleDetailRepository;
import com.toel.repository.FlashSaleRepository;

@RestController
@CrossOrigin("*")
@RequestMapping("api/v1/user/shop/")
public class ShowInfoSeller {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    FlashSaleRepository flashSaleRepo;
    @Autowired
    Service_SelectFlashSale service_SelectFlashSale;
    @Autowired
    Service_SelectAllProductHome serviceSellectAll;
    @Autowired
    FlashSaleDetailRepository flashSaleDetailRepo;

    @RequestMapping("selectall")
    public ApiResponse<Map<String, Object>> selectAll(
            @RequestParam(name = "id_Shop", defaultValue = "0") Integer id_Shop,
            @RequestParam(name = "size", defaultValue = "8") Integer size,
            @RequestParam(name = "sort", defaultValue = "price") String sort) {
        List<FlashSaleDetail> flashSaleDetails = new ArrayList<FlashSaleDetail>();
        LocalDateTime localDateTime = LocalDateTime.now();
        FlashSale flashSale = flashSaleRepo.findFlashSaleNow(localDateTime);
        try {

            flashSaleDetails = flashSaleDetailRepo.findAllByFlashSale(flashSale);
        } catch (Exception e) {
        }

        Map<String, Object> response = serviceSellectAll.selectAllHomeShop(flashSaleDetails, id_Shop, 0, size, sort);
        response.put("flashSale", flashSale);
        if (response.get("error") != null) {
            return ApiResponse.<Map<String, Object>>build().message("not fault").code(1002);
        }

        return ApiResponse.<Map<String, Object>>build().message("success").result(response);
    }
}
