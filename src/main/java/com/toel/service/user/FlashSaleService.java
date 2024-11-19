package com.toel.service.user;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toel.dto.user.response.Response_FlashSaleDetail;
import com.toel.mapper.user.FlashSaleDetailMapper;
import com.toel.model.FlashSale;
import com.toel.model.FlashSaleDetail;
import com.toel.repository.FlashSaleRepository;

@Service
public class FlashSaleService {
	@Autowired
	private FlashSaleRepository flashSaleRepo;


	public FlashSaleDetail getFlashSaleDetailForProduct(Integer productId) {
		LocalDateTime now = LocalDateTime.now();
		FlashSale flashSale = flashSaleRepo.findFlashSaleNow(now);

		if (flashSale == null || flashSale.getFlashSaleDetails() == null) {
			return null;
		}

		for (FlashSaleDetail item : flashSale.getFlashSaleDetails()) {
			if (item.getProduct() != null && item.getProduct().getId().equals(productId)) {

				return item;

			}
		}
		return null;
	}
}
