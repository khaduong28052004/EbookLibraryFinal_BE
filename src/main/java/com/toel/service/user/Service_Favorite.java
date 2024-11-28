package com.toel.service.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toel.dto.user.response.Response_Favorite;
import com.toel.mapper.user.FavoriteMapper;
import com.toel.mapper.user.ProductMaperUser;
import com.toel.model.Account;
import com.toel.model.Like;
import com.toel.model.Product;
import com.toel.repository.AccountRepository;
import com.toel.repository.LikeRepository;
import com.toel.repository.ProductRepository;

@Service
public class Service_Favorite {
	@Autowired
	LikeRepository likeRepo;
	@Autowired
	ProductRepository productRepo;
	@Autowired
	AccountRepository accountRepo;

	@Autowired
	FavoriteMapper favoriteMapper;

	@Autowired
	ProductMaperUser productMaperUser;

	@Autowired
	FlashSaleService flashSaleService;

	public boolean addFavorite(Integer id_user, Integer id_product) {
		Account user = accountRepo.findById(id_user).get();
		Product product = productRepo.findById(id_product).get();
		try {
			Like like = likeRepo.findByAccountAndProduct(user, product);
			if (like != null) {
				likeRepo.delete(like);
				return false;
			}

		} catch (Exception e) {
		}
		Like like = new Like();
		like.setProduct(product);
		like.setAccount(user);
		like.setCreateAt(new Date());
		likeRepo.save(like);
		return true;

	}

	public boolean checkFavorite(Integer id_user, Integer id_product) {
		Map<String, Object> response = new HashMap<>();
		Account user = accountRepo.findById(id_user).get();
		Product product = productRepo.findById(id_product).get();
		try {
			Like like = likeRepo.findByAccountAndProduct(user, product);
			if (like != null) {
				return true;
			}

		} catch (Exception e) {
		}
		return false;
	}

	public Map<String, Object> getAllFavorite(Integer id_user) {
		Account user = accountRepo.findById(id_user).get();
		List<Like> listLikes = likeRepo.findAllByAccount(user);
		List<Response_Favorite> list_Response_Favorites = new ArrayList<Response_Favorite>();
		for (Like like : listLikes) {
			Response_Favorite response_Favorite = favoriteMapper.response_FavoriteToLike(like);
			response_Favorite.setProduct(productMaperUser.productToResponse_Product(like.getProduct()));
			response_Favorite.getProduct().setFlashSaleDetail(
					flashSaleService.getFlashSaleDetailForProduct(response_Favorite.getProduct().getId()));
			list_Response_Favorites.add(response_Favorite);
		}
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("datas", list_Response_Favorites);
		return response;
	}

	public Boolean deleteFavorite(Integer id) {
		try {
			likeRepo.deleteById(id);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

}
