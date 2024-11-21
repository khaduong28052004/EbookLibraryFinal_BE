package com.toel.service.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.toel.dto.user.response.Response_Product;
import com.toel.mapper.user.ProductMaperUser;
import com.toel.model.Category;
import com.toel.model.Product;
import com.toel.repository.CategoryRepository;
import com.toel.repository.ProductRepository;

@Service
public class Service_Search {
	@Autowired
	ProductRepository productRepo;
	List<Product> listProducts = new ArrayList<Product>();
	List<Product> listProductByCategory = new ArrayList<Product>();
	@Autowired
	ProductMaperUser productMaperUser;
	@Autowired
	CategoryRepository categoryRepo;

	public Map<String, Object> getProductByName(String name, Integer size, String sort) {
		Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, sort));
		Page<Product> pageProducts = productRepo.findAllByNameContainingAndIsActiveTrueAndIsDeleteFalse(name, pageable);
		listProducts = pageProducts.getContent();
		List<Response_Product> listResponse_Products = new ArrayList<Response_Product>();
		Map<String, Object> response = new HashMap<String, Object>();
		Map<Integer, Category> categoryMap = new HashMap<Integer, Category>();
		for (Product product : pageProducts) {
			listResponse_Products.add(productMaperUser.productToResponse_Product(product));
			categoryMap.computeIfAbsent(product.getCategory().getId(), id -> product.getCategory());
		}
		response.put("datas", listResponse_Products);
		response.put("categories", categoryMap.values());
		return response;
	}

	public Map<String, Object> filterProductByCategory(List<Integer> id_categories, Integer size, String sort) {
		Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, sort));
		List<Category> listCategories = new ArrayList<Category>();
		for (Integer id_catgory : id_categories) {
			listCategories.add(categoryRepo.findById(id_catgory).get());
		}
		List<Integer> listIdProducts = listProducts.stream().map(product -> product.getId())
				.collect(Collectors.toList());
		Page<Product> pageProducts = productRepo.findByCategoryInAndIdIn(listCategories, listIdProducts, pageable);

		listProductByCategory = pageProducts.getContent();

		List<Response_Product> listResponse_Products = new ArrayList<Response_Product>();
		Map<String, Object> response = new HashMap<String, Object>();
		for (Product product : pageProducts) {
			listResponse_Products.add(productMaperUser.productToResponse_Product(product));
		}
		response.put("datas", listResponse_Products);
		return response;
	}

	public Map<String, Object> filterProductByPrice(double priceMin, double priceMax, Integer size, String sort) {
		Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, sort));

		List<Integer> listIdProducts = listProducts.stream().map(product -> product.getId())
				.collect(Collectors.toList());
		Page<Product> pageProducts = productRepo.findByPriceBetweenAndIdIn(priceMin, priceMax, listIdProducts,
				pageable);

		listProductByCategory = pageProducts.getContent();

		List<Response_Product> listResponse_Products = new ArrayList<Response_Product>();
		Map<String, Object> response = new HashMap<String, Object>();
		for (Product product : pageProducts) {
			listResponse_Products.add(productMaperUser.productToResponse_Product(product));
		}
		response.put("datas", listResponse_Products);
		return response;
	}
	
	

}
