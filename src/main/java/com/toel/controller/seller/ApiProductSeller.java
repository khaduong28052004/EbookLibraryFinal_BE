package com.toel.controller.seller;

import java.io.IOException;

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
import com.toel.dto.seller.request.Product.Request_ProductCreate;
import com.toel.dto.seller.request.Product.Request_ProductUpdate;
import com.toel.dto.seller.request.Voucher.Request_VoucherCreate;
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
                        @RequestParam(value = "account_id", defaultValue = "1") Integer account_id,
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                        @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
                        @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
                return ApiResponse.<PageImpl<Response_Product>>build()
                                .result(service_ProductSeller.getAll(page, size, sortBy, sortColumn, account_id));
        }

        @PostMapping("/create")
        public ApiResponse<Response_Product> create(
                        @RequestBody @Valid Request_ProductCreate request_Product) throws IOException {
                return ApiResponse.<Response_Product>build()
                                .message("Thêm sản phẩm thành công")
                                .result(service_ProductSeller.create(request_Product));
        }

        @PostMapping("/update")
        public ApiResponse<Response_Product> update(
                        @RequestBody @Valid Request_ProductUpdate request_Product) throws IOException {
                return ApiResponse.<Response_Product>build()
                                .message("Cập nhật sản phẩm thành công")
                                .result(service_ProductSeller.update(request_Product));
        }

        @DeleteMapping("/delete")
        public ApiResponse delete(@RequestParam("product_id") Integer product_id) {
                service_ProductSeller.delete(product_id);
                return ApiResponse.build()
                                .message("Xóa sản phẩm thành công");
        }
}