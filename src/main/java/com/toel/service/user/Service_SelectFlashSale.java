package com.toel.service.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.toel.dto.user.response.Response_FlashSaleDetail;
import com.toel.mapper.user.FlashSaleDetailMapper;
import com.toel.model.FlashSale;
import com.toel.model.FlashSaleDetail;
import com.toel.repository.FlashSaleDetailRepository;
import com.toel.repository.FlashSaleRepository;
import com.toel.repository.ProductRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Service_SelectFlashSale {

	final FlashSaleRepository flashSaleRepo;
	final FlashSaleDetailRepository flashSaleDetailRepo;
	final FlashSaleDetailMapper flashSaleDetailMapper;

	public List<Response_FlashSaleDetail> selectFlashSale(Integer id_Shop) {
		LocalDateTime localDateTime = LocalDateTime.now();
		FlashSale flashSale = flashSaleRepo.findFlashSaleNow(localDateTime).getFirst();
		List<FlashSaleDetail> flashSaleDetails = flashSaleDetailRepo.findAllByFlashSale(flashSale);
		List<Response_FlashSaleDetail> response_FlashSaleDetails = new ArrayList<Response_FlashSaleDetail>();
		for (FlashSaleDetail item : flashSaleDetails) {
			if (item.getProduct().getAccount().getId() != id_Shop && item.getProduct().isActive()
					&& item.getProduct().isDelete() == false) {
				Response_FlashSaleDetail response_FlashSaleDetail = flashSaleDetailMapper
						.flashSaleDetailToResponseFlashSaleDetail(item);
				response_FlashSaleDetails.add(response_FlashSaleDetail);
			}
		}
		return response_FlashSaleDetails;
	};

}
