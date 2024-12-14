package com.toel.controller.seller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.seller.response.Response_Account;
import com.toel.service.admin.Service_Product;
import com.toel.service.seller.Service_ShopSeller;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/seller/shop")
public class ApiShopSeller {

        @Autowired
        Service_ShopSeller service_Shop;
        @Autowired
        Service_Product productService;

        @GetMapping("/followers/count")
        public ApiResponse<Integer> countFollowers(
                        @RequestParam(value = "shop_id", required = true) Integer shopId) {
                return ApiResponse.<Integer>build()
                                .result(service_Shop.countFollowersByShopId(shopId));
        }

        @GetMapping("/following/count")
        public ApiResponse<Integer> countFollowing(
                        @RequestParam(value = "account_id", required = true) Integer accountId) {
                return ApiResponse.<Integer>build()
                                .result(service_Shop.countFollowingByAccountId(accountId));
        }

        @GetMapping("/posts/count")
        public ApiResponse<Integer> countPosts(
                        @RequestParam(value = "account_id", required = true) Integer accountId) {
                return ApiResponse.<Integer>build()
                                .result(productService.getCountProductByAccountId(accountId));
        }

        @GetMapping("/get")
        public ApiResponse<Response_Account> get(
                        @RequestParam(value = "account_id", defaultValue = "0") Integer account_id) {
                return ApiResponse.<Response_Account>build()
                                .result(service_Shop.get(account_id));
        }
}
