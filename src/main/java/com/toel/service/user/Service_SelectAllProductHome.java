package com.toel.service.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
import com.toel.model.Follower;
import com.toel.model.Like;
import com.toel.model.Product;
import com.toel.repository.AccountRepository;
import com.toel.repository.BillDetailRepository;
import com.toel.repository.BillRepository;
import com.toel.repository.FollowerRepository;
import com.toel.repository.LikeRepository;
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
	final FollowerRepository followerRepository;
	final LikeRepository likeRepository;
	final ProductMaperUser productMaperUser;
	List<Integer> idProducts = new ArrayList<>();

	public Map<String, Object> selectAll(List<FlashSaleDetail> list, Integer idShop, Integer page, Integer size,
			String sort) {

		idProducts.addAll(list.stream().map(p -> p.getProduct().getId()).collect(Collectors.toList()));
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

	public Map<String, Object> selectAllHomeShop(List<FlashSaleDetail> list, Integer idShop, Integer page, Integer size,
			String sort) {

		// List<Integer> idProducts = list.stream().map(p ->
		// p.getProduct().getId()).collect(Collectors.toList());
		Pageable pageable = PageRequest.of(0, size);
		Page<Product> pageProducts = productRepo.findAllIdIn(idShop, pageable);
		List<Response_Product> response_Products = new ArrayList<Response_Product>();
		for (Product product : pageProducts) {
			if (product.getAccount().getId() == idShop) {
				response_Products.add(productMaper.productToResponse_Product(product));
			}
		}
		Map<String, Object> response = new HashMap<String, Object>();
		if (response_Products.size() > 0) {
			response.put("datas", response_Products);
			// response.put("totalPages", pageProducts.getTotalPages() *
			// response_Products.size());
		} else {
			response.put("error", 1002);
		}
		return response;

	}

	// =======
	// // response.put("totalPages", pageProducts.getTotalPages() *
	// // response_Products.size());
	// } else {
	// response.put("error", 1002);
	// }
	// return response;
	// }

	// public Map<String, Object> selectAllHomeShop(List<FlashSaleDetail> list,
	// Integer idShop, Integer page, Integer size,
	// String sort) {

	// // List<Integer> idProducts = list.stream().map(p ->
	// // p.getProduct().getId()).collect(Collectors.toList());
	// Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
	// List<Product> pageProducts = productRepo.findAllIdIn(idShop);
	// List<Response_Product> response_Products = new ArrayList<Response_Product>();
	// for (Product product : pageProducts) {
	// if (product.getAccount().getId() == idShop) {
	// response_Products.add(productMaper.productToResponse_Product(product));
	// }
	// }
	// Map<String, Object> response = new HashMap<String, Object>();
	// if (response_Products.size() > 0) {
	// response.put("datas", response_Products);
	// // response.put("totalPages", pageProducts.getTotalPages() *
	// // response_Products.size());
	// >>>>>>> kienv2

	// Gợi ý
	public List<Response_Product> suggestProduct(Integer id_user) {

		List<Follower> listFollowers = new ArrayList<Follower>();
		List<Like> listLikes = new ArrayList<Like>();
		List<Product> listProductByBills = new ArrayList<>();
		if (id_user == 0) {
			Pageable pageable = PageRequest.of(0, 5);
			List<Integer> listIdTopLikes = likeRepository.selectIdLikeByTopProductLike(pageable);
			listLikes = likeRepository.selectAllByListID(listIdTopLikes);

			List<Integer> listIdProductByBillDetails = productRepo.selectIdBillDetailTopProduct(pageable);
			listProductByBills = productRepo.selectProductInIdProduct(listIdProductByBillDetails);
		} else {
			Account user = accountRepository.findById(id_user).get();
			listFollowers = followerRepository.findAllByAccount(user);
			listLikes = likeRepository.findAllByAccount(user);

			if (listFollowers.size() + listLikes.size() < 8) {
				List<Integer> ids = new ArrayList<Integer>();
				ids.addAll(productRepo.findAllByBillOfUser(id_user).stream().map(product -> product.getId())
						.collect(Collectors.toList()));
				ids.addAll(listLikes.stream().map(product -> product.getId()).collect(Collectors.toList()));

				for (Follower follow : listFollowers) {
					Account shop = accountRepository.findById(follow.getShopId()).get();
					if (shop.isStatus()) {
						for (Product product : shop.getProducts().subList(0, 1)) {
							if (product.isActive() && product.isDelete() == false) {
								ids.add(product.getId());
							}
						}
					}
//					ids.addAll(productRepo.findAllIdIn(shop.getId()).stream().map(product -> product.getId())
//							.collect(Collectors.toList()));

				}
				Pageable pageable = PageRequest.of(0, 8 - (listFollowers.size() + listLikes.size()));
				List<Integer> listIdProductByBillDetails = productRepo.selectIdBillDetailTopProductNotIds(idProducts,
						pageable);
				listProductByBills = productRepo.selectProductInIdProduct(listIdProductByBillDetails);
			}

		}

		List<Product> listProducts = new ArrayList<>();
		for (Follower follow : listFollowers) {
			Account shop = accountRepository.findById(follow.getShopId()).get();
			if (shop.isStatus()) {
				for (Product product : shop.getProducts().subList(0, 1)) {
					if (product.isActive() && product.isDelete() == false) {
						listProducts.add(product);
					}
				}
			}
		}
		List<Integer> listIdProduct = listProducts.stream().map(Product::getId).collect(Collectors.toList());

		for (Like like : listLikes) {
			if (!listIdProduct.contains(like.getProduct().getId())) {
				listProducts.add(like.getProduct());
			}
		}
		listIdProduct.addAll(listProducts.stream().map(Product::getId).collect(Collectors.toList()));

		for (Product product : listProductByBills) {
			if (!listIdProduct.contains(product.getId())) {
				listProducts.add(product);
			}
		}
		Collections.shuffle(listProducts);
		List<Response_Product> listResponse_Products = new ArrayList<Response_Product>();
		for (Product product : listProducts.size() > 8 ? listProducts.subList(0, 8)
				: listProducts.subList(0, listProducts.size())) {
			listResponse_Products.add(productMaperUser.productToResponse_Product(product));
			idProducts.add(product.getId());
		}
		return listResponse_Products;
	}
}
