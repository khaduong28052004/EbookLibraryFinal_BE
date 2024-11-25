package com.toel.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.service.user.FollowerService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin("*")
@RequestMapping("api/v1/user/follower")
public class ApiFollower {
	@Autowired
	FollowerService followerService;

	@GetMapping("check")
	public ApiResponse<Boolean> checkFoller(@RequestParam("id_user") Integer id_user,
			@RequestParam("id_shop") Integer id_shop) {
		return ApiResponse.<Boolean>build().message("fetch follower success.")
				.result(followerService.checkFollower(id_user, id_shop));
	}

	@GetMapping("create")
	public ApiResponse<Boolean> createFoller(@RequestParam("id_user") Integer id_user,
			@RequestParam("id_shop") Integer id_shop) {
		return ApiResponse.<Boolean>build().message("create follower success.")
				.result(followerService.createFollower(id_user, id_shop));
	}

}
