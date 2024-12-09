package com.toel.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.toel.repository.AccountRepository;
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

    @Scheduled(fixedDelay = 86400)
    // @Scheduled(fixedDelay = 100)
    public void run() {
        productRepository.findAllCreatedBeforeSevenDays().forEach(product -> {
            service_Product.updateActive(product.getId(), true, null);
        });
        accountRepository.listAccountBeforeSevenDays().forEach(account -> {
            service_Account.updateActive(account.getId(), true,null);
        });
    }
}
