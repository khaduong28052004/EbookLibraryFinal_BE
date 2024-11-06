package com.toel.service.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.toel.dto.user.response.Response_FlashSaleDetail;
import com.toel.dto.user.response.Response_Product;
import com.toel.mapper.user.ProductMaper;
import com.toel.model.Product;
import com.toel.repository.ProductRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE)
public class Service_SelectAllProductHome {
	final ProductRepository productRepo;
	final ProductMaper productMaper;

	public Map<String, Object> selectAll(List<Response_FlashSaleDetail> list, Integer idShop, Integer page,
			Integer size, String sort) {

		List<Integer> idProducts = list.stream().map(p -> p.getProduct().getId()).collect(Collectors.toList());
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
		Page<Product> pageProducts = productRepo.findAllIdNotIn(idProducts, pageable);
		List<Response_Product> response_Products = new ArrayList<Response_Product>();
		for (Product product : pageProducts) {
			if (product.getAccount().getId() != idShop) {
				response_Products.add(productMaper.productToResponse_Product(product));
			}
		}
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("data", response_Products);
		response.put("totalPages", pageProducts.getTotalPages());
		return response;

	}

}
