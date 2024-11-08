package com.toel.service.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.toel.dto.user.response.Response_FlashSaleDetail;
import com.toel.mapper.user.FlashSaleDetailMapper;
import com.toel.model.FlashSale;
import com.toel.model.FlashSaleDetail;
import com.toel.repository.FlashSaleDetailRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Service_SelectFlashSale {

	final FlashSaleDetailRepository flashSaleDetailRepo;
	final FlashSaleDetailMapper flashSaleDetailMapper;

	public List<Response_FlashSaleDetail> selectFlashSale(FlashSale flashSale, Integer id_Shop) {

		List<Response_FlashSaleDetail> response_FlashSaleDetails = new ArrayList<Response_FlashSaleDetail>();

		List<FlashSaleDetail> flashSaleDetails = flashSaleDetailRepo.findAllByFlashSale(flashSale);

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
