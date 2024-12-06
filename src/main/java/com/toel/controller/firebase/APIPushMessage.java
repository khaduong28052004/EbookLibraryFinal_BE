package com.toel.controller.firebase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.service.firebase.MessageService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/seller")
public class APIPushMessage {

    @Autowired
    MessageService messageService;

    @GetMapping("/pushMessage")
    public ApiResponse<?> send() {
        return ApiResponse.build()
                .result(messageService.pushMessage(3, "xin chao"));
    }

}
