package com.toel.service.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.toel.dto.user.response.Response_Evalue;
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
		List<Response_Evalue> evalues = new ArrayList<>();
		Set<Integer> idEvalues = new HashSet<>(); // Sử dụng Set để tránh trùng lặp ID

		if (product.getEvalues() != null) { // Kiểm tra null
			for (Response_Evalue item : product.getEvalues()) {
				if (!idEvalues.contains(item.getId())) {
					evalues.add(item); // Thêm item vào danh sách kết quả
					idEvalues.add(item.getId()); // Lưu ID để tránh trùng lặp

					// Tìm các item con (idParent khớp với ID của item)
					for (Response_Evalue item2 : product.getEvalues()) {
						if (item2.getIdParent() == item.getId() && !idEvalues.contains(item2.getId())) {
							evalues.add(item2); // Thêm item con
							idEvalues.add(item2.getId()); // Đánh dấu ID của item con
						}
					}
				}
			}
		}

		product.setEvalues(evalues);
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
