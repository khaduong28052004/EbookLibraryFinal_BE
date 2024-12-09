package com.toel.controller.seller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.admin.response.Response_ProductListFlashSale;
import com.toel.dto.user.response.Response_Product;
import com.toel.service.seller.Service_ChatBot;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/chatBot")
public class ApiChatBot {
    @Autowired
    Service_ChatBot service_ChatBot;

    @GetMapping("/search")
    public ApiResponse<List<Response_Product>> search(
            @RequestParam(value = "key", required = false) String key) {
        return ApiResponse.<List<Response_Product>>build()
                .result(service_ChatBot.searchChatBot(key));
    }
}
