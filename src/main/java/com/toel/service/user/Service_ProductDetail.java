package com.toel.service.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.toel.dto.user.response.Response_Product;
import com.toel.mapper.user.ProductMaperUser;
import com.toel.model.Account;
import com.toel.model.Category;
import com.toel.model.Product;
import com.toel.repository.CategoryRepository;
import com.toel.repository.ProductRepository;

@Service
public class Service_ProductDetail {
	@Autowired
	ProductRepository productRepo;
	@Autowired
	ProductMaperUser productMaper;
	@Autowired
	CategoryRepository categoryRepo;
	@Autowired
	FlashSaleService flashSaleService;
	Response_Product product = new Response_Product();
	Account seller = new Account();

	public Map<String, Object> getProduct(Integer id_Product) {
		product = productMaper.productToResponse_Product(productRepo.findById(id_Product).get());
		product.setFlashSaleDetail(flashSaleService.getFlashSaleDetailForProduct(product.getId()));
		seller = product.getAccount();
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("seller", seller);
		response.put("product", product);
		return response;
	}

	public Map<String, Object> getAllRelated(Integer id_User, Integer size, String sort) {
		Map<String, Object> response = new HashMap<>();
		Pageable pageable = PageRequest.of(0, size, Sort.by(sort));
		Page<Product> pageProducts = productRepo.findAllByIdNotAndCategory(product.getId(), product.getCategory(),
				pageable);
		List<Response_Product> response_Products = new ArrayList<>();
		for (Product product : pageProducts) {
			if (product.isActive() && product.isDelete() == false && product.getQuantity() > 0
					&& product.getAccount().getId() != id_User) {
				response_Products.add(productMaper.productToResponse_Product(product));
			}
		}
		response.put("datas", response_Products);
		response.put("totalPages", (pageProducts.getTotalPages() * pageProducts.getContent().size()));
		return response;
	}

	public Map<String, Object> getAllProduct(Integer size, String sort) {
		Map<String, Object> response = new HashMap<>();
		Pageable pageable = PageRequest.of(0, size, Sort.by(sort));
		Page<Product> pageProducts = productRepo.findAllByAccountAndCategoryNot(seller, product.getCategory(),
				pageable);
		List<Response_Product> response_Products = new ArrayList<>();
		for (Product product : pageProducts) {
			response_Products.add(productMaper.productToResponse_Product(product));
		}
		response.put("datas", response_Products);
		response.put("totalPages", (pageProducts.getTotalPages() * pageProducts.getContent().size()));
		return response;
	}

}
