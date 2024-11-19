package com.toel.controller.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.service.user.Service_Favorite;

@RestController
@CrossOrigin("*")
@RequestMapping("api/v1/user")
public class ApiFavorite {
	@Autowired
	Service_Favorite service_Favorite;

	@RequestMapping("favorite/check")
	public ApiResponse<Boolean> check(@RequestParam("id_user") Integer id_user,
			@RequestParam("id_product") Integer id_product) {
		return ApiResponse.<Boolean>build().result(service_Favorite.checkFavorite(id_user, id_product));
	}

	@RequestMapping("favorite/add")
	public ApiResponse<Boolean> add(@RequestParam("id_user") Integer id_user,
			@RequestParam("id_product") Integer id_product) {
		return ApiResponse.<Boolean>build().result(service_Favorite.addFavorite(id_user, id_product));
	}

	@RequestMapping("favorite/getall/{id}")
	public ApiResponse<Map<String, Object>> getAll(@PathVariable("id") Integer id_user) {
		return ApiResponse.<Map<String, Object>>build().result(service_Favorite.getAllFavorite(id_user));
	}
}
