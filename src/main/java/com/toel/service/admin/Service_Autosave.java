package com.toel.service.admin;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.toel.model.DiscountRate;
import com.toel.repository.AccountRepository;
import com.toel.repository.DiscountRateRepository;
import com.toel.repository.FlashSaleRepository;
import com.toel.repository.LogRepository;
import com.toel.repository.ProductRepository;

@Service
public class Service_Autosave {
    @Autowired
    Service_Product service_Product;
    @Autowired
    Service_Account service_Account;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    LogRepository logRepository;
    @Autowired
    Service_Log service_Log;
    @Autowired
    FlashSaleRepository flashSaleRepository;
    @Autowired
    DiscountRateRepository discountRateRepository;

    @Scheduled(fixedDelay = 86400)
    // @Scheduled(fixedDelay = 100)
    public void run() {
        productRepository.findAllCreatedBeforeSevenDays().forEach(product -> {
            service_Product.updateActive(product.getId(), true, null, null);
        });
        accountRepository.listAccountBeforeSevenDays().forEach(account -> {
            service_Account.updateActive(account.getId(), true, null, null);
        });
        service_Log.deleteList(logRepository.selectAllCreatedBeforeSevenDays());
    }

    @Scheduled(fixedDelay = 60000)
    // @Scheduled(fixedDelay = 100)
    public void deleteFlashsale() {
        if (flashSaleRepository.findByIsDelete(false).size() >= 1) {
            flashSaleRepository.findByIsDelete(false).forEach(flasesale -> {
                if (flasesale.getDateEnd().isBefore(LocalDateTime.now())) {
                    flasesale.setDelete(true);
                    flashSaleRepository.save(flasesale);
                }
            });
        }
    }

    @Scheduled(fixedDelay = 60000)
    // @Scheduled(fixedDelay = 100)
    public void deleteDiscountRate() {
        if (discountRateRepository.findAllBydateDeleteIsNull().size() >= 2) {
            DiscountRate discountRate = discountRateRepository.findLatestDiscountRate().get(0);
            discountRateRepository.findAllBydateDeleteIsNull().forEach(rate -> {
                if (rate.getDateStart().isBefore(LocalDateTime.now()) && rate.getId() != discountRate.getId())
                    rate.setDateDelete(LocalDateTime.now());
                discountRateRepository.save(rate);
            });
        }
    }
}
