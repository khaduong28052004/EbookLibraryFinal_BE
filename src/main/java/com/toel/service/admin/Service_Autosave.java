package com.toel.service.admin;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.toel.model.DiscountRate;
import com.toel.model.FlashSale;
import com.toel.model.FlashSaleDetail;
import com.toel.model.Product;
import com.toel.repository.AccountRepository;
import com.toel.repository.DiscountRateRepository;
import com.toel.repository.FlashSaleDetailRepository;
import com.toel.repository.FlashSaleRepository;
import com.toel.repository.LogRepository;
import com.toel.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
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
    @Autowired
    FlashSaleDetailRepository flashSaleDetailRepository;

    @Scheduled(fixedDelay = 604800016) //1 tuần
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

    @Scheduled(fixedDelay = 60000) // 1 phút
    // @Scheduled(fixedDelay = 100)
    public void deleteFlashsale() {
        if (flashSaleRepository.findByIsDelete(false).size() >= 1) {
            flashSaleRepository.findByIsDelete(false).forEach(flasesale -> {
                if (flasesale.getDateEnd().isBefore(LocalDateTime.now())) {
                    for (FlashSaleDetail flashSaleDetail : flasesale.getFlashSaleDetails()) {
                        Product product = flashSaleDetail.getProduct();
                        int availableQuantity = product.getQuantity();
                        flashSaleDetail.setQuantity(0);
                        flashSaleDetailRepository.save(flashSaleDetail);

                        product.setQuantity(availableQuantity + flashSaleDetail.getQuantity());
                        productRepository.save(product);
                    }
                    flasesale.setDelete(true);
                    flashSaleRepository.save(flasesale);
                }
            });
        }
    }

    @Scheduled(fixedDelay = 60000) //1 phút
    // @Scheduled(fixedDelay = 100)
    public void ApdungFlashsale() {
        Optional<FlashSale> optionalFlashSale = flashSaleRepository.selectFlashSaleNow(LocalDateTime.now());
        if (optionalFlashSale.isPresent()) {
            FlashSale flashSale = optionalFlashSale.get();
            flashSale.getFlashSaleDetails().size();
            for (FlashSaleDetail flashSaleDetail : flashSale.getFlashSaleDetails()) {
                Product product = flashSaleDetail.getProduct();
                int availableQuantity = product.getQuantity();

                if (flashSaleDetail.getQuantity() > availableQuantity) {
                    flashSaleDetail.setQuantity(availableQuantity);
                    flashSaleDetailRepository.save(flashSaleDetail);

                    product.setQuantity(0);
                    productRepository.save(product);
                } else {
                    int remainingQuantity = availableQuantity - flashSaleDetail.getQuantity();
                    product.setQuantity(remainingQuantity);
                    productRepository.save(product);
                }
            }
        } else {
            // Log or handle the absence of an active FlashSale
            System.out.println("No active Flash Sale found at " + LocalDateTime.now());
        }
    }

    @Scheduled(fixedDelay = 172800000) //1 ngày
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
