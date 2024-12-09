package com.toel.service.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.toel.dto.user.response.Response_FlashSaleDetail;
import com.toel.mapper.user.FlashSaleDetailMapper;
import com.toel.model.FlashSale;
import com.toel.model.FlashSaleDetail;
import com.toel.repository.FlashSaleDetailRepository;
import com.toel.repository.FlashSaleRepository;

@Service
public class FlashSaleService {
	@Autowired
	private FlashSaleRepository flashSaleRepo;
	@Autowired
	private FlashSaleDetailRepository flashSaleDetailRepository;
	@Autowired
	FlashSaleDetailMapper flashSaleDetailMapper;

	public FlashSaleDetail getFlashSaleDetailForProduct(Integer productId) {
		LocalDateTime now = LocalDateTime.now();
		FlashSale flashSale = flashSaleRepo.findFlashSaleNow(now);

		if (flashSale == null || flashSale.getFlashSaleDetails() == null) {
			return null;
		}

		for (FlashSaleDetail item : flashSale.getFlashSaleDetails()) {
			if (item.getProduct() != null && item.getProduct().getId().equals(productId) && item.getQuantity() > 0) {
				return item;

			}
		}
		return null;
	}

	public List<FlashSale> getAllFlashSaleByNow() {
		LocalDateTime now = LocalDateTime.now();
		Pageable pageable = PageRequest.of(0, 5, Sort.by(Direction.ASC, "dateStart"));
		Page<FlashSale> pageFlashSale = flashSaleRepo.findAllByNow(now, pageable);
		return pageFlashSale.getContent();
	}

	public List<Response_FlashSaleDetail> getAllFlashSaleDetailByFlashSale(Integer id_flashsale) {
		FlashSale flashSale = flashSaleRepo.findById(id_flashsale).get();
		List<FlashSaleDetail> listFlashSaleDetails = flashSaleDetailRepository.findAllByFlashSale(flashSale);
		List<Response_FlashSaleDetail> listResponse_FlashSaleDetails = listFlashSaleDetails.stream()
				.map(flashSaleDetail -> {
					Response_FlashSaleDetail response_FlashSaleDetail = flashSaleDetailMapper
							.flashSaleDetailToResponseFlashSaleDetail(flashSaleDetail);
					response_FlashSaleDetail.getProduct().setFlashSaleDetail(flashSaleDetail);
					return response_FlashSaleDetail;
				}).collect(Collectors.toList());
		return listResponse_FlashSaleDetails;
	}
}
