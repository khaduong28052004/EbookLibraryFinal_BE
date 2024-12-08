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

import com.toel.dto.user.response.Response_Product;
import com.toel.mapper.user.ProductMaperUser;
import com.toel.model.Account;
import com.toel.model.Bill;
import com.toel.model.BillDetail;
import com.toel.model.FlashSaleDetail;
import com.toel.model.Product;
import com.toel.repository.AccountRepository;
import com.toel.repository.BillDetailRepository;
import com.toel.repository.BillRepository;
import com.toel.repository.ProductRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE)
public class Service_SelectAllProductHome {
	final ProductRepository productRepo;
	final ProductMaperUser productMaper;
	final BillRepository billRepository;
	final AccountRepository accountRepository;
	final BillDetailRepository billDetailRepository;

	public Map<String, Object> selectAll(List<FlashSaleDetail> list, Integer idShop, Integer page, Integer size,
			String sort) {

		List<Integer> idProducts = list.stream().map(p -> p.getProduct().getId()).collect(Collectors.toList());
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
		Page<Product> pageProducts = productRepo.findAllIdNotIn(idProducts, idShop, pageable);
		List<Response_Product> response_Products = new ArrayList<Response_Product>();
		for (Product product : pageProducts) {
			if (product.getAccount().getId() != idShop) {
				response_Products.add(productMaper.productToResponse_Product(product));
			}
		}
		Map<String, Object> response = new HashMap<String, Object>();
		if (response_Products.size() > 0) {
			response.put("datas", response_Products);
			response.put("totalPages", pageProducts.getTotalPages());
		} else {
			response.put("error", 1002);
		}
		return response;
	}

//	 Gợi ý
	public List<Response_Product> suggestProduct(Integer id_user) {
		Account account = accountRepository.findById(id_user).get();
		List<BillDetail> listBillDetails = billDetailRepository.findAllByUser(id_user);
		List<Response_Product> listResponse_Products = new ArrayList<Response_Product>();
		return listResponse_Products;
	}
}
