package com.toel.controller.seller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.seller.request.Request_Product;
import com.toel.dto.seller.request.Request_Voucher;
import com.toel.dto.seller.response.Response_Product;
import com.toel.dto.seller.response.Response_Voucher;
import com.toel.service.seller.Service_ProductSeller;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/seller/product")
public class ApiProductSeller {

    @Autowired
    Service_ProductSeller service_ProductSeller;

    @GetMapping("/getAll")
    public ApiResponse<PageImpl<Response_Product>> getAll(
            @RequestParam(value = "account_id", defaultValue = "0") Integer account_id,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
            @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
        return ApiResponse.<PageImpl<Response_Product>>build()
                .result(service_ProductSeller.getAll(page, size, sortBy, sortColumn, account_id));
    }

    @PostMapping("/save")
    public ApiResponse<Response_Product> save(
            @RequestBody @Valid Request_Product request_Product) {
        Response_Product response_Product = service_ProductSeller.save(request_Product);
        return ApiResponse.<Response_Product>build()
                .message(response_Product.getId() == null ? "Thêm sản phẩm thành công"
                        : "Cập nhật sản phẩm thành công")
                .result(response_Product);
    }

    @DeleteMapping("/delete")
    public ApiResponse delete(@RequestParam("product_id") Integer product_id) {
        service_ProductSeller.delete(product_id);
        return ApiResponse.build()
                .message("Xóa sản phẩm thành công");
    }
}