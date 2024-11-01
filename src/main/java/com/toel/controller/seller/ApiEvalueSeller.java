package com.toel.controller.seller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.seller.request.Request_Evalue;
import com.toel.dto.seller.response.Response_Evalue;
import com.toel.service.seller.Service_EvalueSeller;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/seller/evalue")
public class ApiEvalueSeller {

    @Autowired
    Service_EvalueSeller service_Evalue;

    @GetMapping("/getAll")
    public ApiResponse<PageImpl<Response_Evalue>> getALl(
            @RequestParam(value = "account_id", defaultValue = "0") Integer account_id,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "sortBy", defaultValue = "true") boolean sortBy,
            @RequestParam(value = "sortColum", defaultValue = "id") String sortColum) {
        return ApiResponse.<PageImpl<Response_Evalue>>build()
                .result(service_Evalue.getAll(page, size, sortBy, sortColum, account_id));
    }

    @PostMapping("/phanHoi")
    public ApiResponse<Response_Evalue> phanHoi(
            @RequestBody @Valid Request_Evalue request_Evalue) {
        return ApiResponse.<Response_Evalue>build()
                .message("Phản hồi đánh giá thành công")
                .result(service_Evalue.phanHoi(request_Evalue));
    }
}
