package com.toel.controller.seller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.admin.response.Response_ProductListFlashSale;
import com.toel.dto.seller.response.Response_Bill;
import com.toel.dto.seller.response.Response_Category;
import com.toel.dto.seller.response.Response_ChatBotCategory;
import com.toel.dto.user.response.Response_Product;
import com.toel.service.seller.Service_ChatBot;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/chatBot")
public class ApiChatBot {
        @Autowired
        Service_ChatBot service_ChatBot;

        @GetMapping("/search")
        public ApiResponse<List<Response_Product>> searchProduct(
                        @RequestParam(value = "key", required = false) String key) {
                return ApiResponse.<List<Response_Product>>build()
                                .result(service_ChatBot.searchChatBotProduct(key));
        }

        @GetMapping("/searchBill")
        public ApiResponse<List<Response_Bill>> searchBill(
                        @RequestParam(value = "key", required = false) String key,
                        @RequestParam(value = "account_id", defaultValue = "0") Integer account_id) {
                return ApiResponse.<List<Response_Bill>>build()
                                .result(service_ChatBot.searchChatBotBill(key, account_id));
        }

        @GetMapping("/searchCategory")
        public ApiResponse<Response_ChatBotCategory> searchCategory(
                        @RequestParam(value = "key", required = false) String key) {
                return ApiResponse.<Response_ChatBotCategory>build()
                                .result(service_ChatBot.searchChatBotCategory(key));
        }
}
