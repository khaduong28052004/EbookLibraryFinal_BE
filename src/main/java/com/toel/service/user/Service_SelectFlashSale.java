package com.toel.service.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	@Autowired
	FlashSaleService flashSaleService;

	public List<Response_FlashSaleDetail> selectFlashSale(FlashSale flashSale, Integer id_Shop) {
		List<Response_FlashSaleDetail> response_FlashSaleDetails = new ArrayList<Response_FlashSaleDetail>();
		Pageable pageable = PageRequest.of(0, 8);
		Page<FlashSaleDetail> flashSaleDetails = flashSaleDetailRepo.findAllByFlashSale(flashSale, id_Shop, pageable);
		for (FlashSaleDetail item : flashSaleDetails.getContent()) {
			if (item.getProduct().getAccount().getId() != id_Shop && item.getProduct().isActive()
					&& item.getProduct().isDelete() == false) {
				Response_FlashSaleDetail response_FlashSaleDetail = flashSaleDetailMapper
						.flashSaleDetailToResponseFlashSaleDetail(item);
				response_FlashSaleDetail.getProduct().setFlashSaleDetail(item);
				response_FlashSaleDetails.add(response_FlashSaleDetail);
			}
		}

		return response_FlashSaleDetails;
	};

}
