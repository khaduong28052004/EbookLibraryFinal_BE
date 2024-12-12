package com.toel.controller.seller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.service.seller.Service_ChatBot;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/chatBot")
public class ApiChatBot {
    @Autowired
    Service_ChatBot service_ChatBot;

    @GetMapping("/search")
    public ApiResponse<List<?>> search(
            @RequestParam(value = "key", required = false) String key) {
        return ApiResponse.<List<?>>build()
                .result(service_ChatBot.searchChatBot(key));
    }

    @GetMapping("/searchBill")
    public ApiResponse<List<?>> search(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "account_id", defaultValue = "0") Integer account_id) {
        return ApiResponse.<List<?>>build()
                .result(service_ChatBot.searchChatBotBill(key, account_id));
    }
}
