package com.toel.controller.seller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.admin.response.Response_ProductListFlashSale;
import com.toel.dto.seller.request.Product.Request_ProductCreate;
import com.toel.dto.seller.request.Product.Request_ProductUpdate;
import com.toel.service.seller.Service_ProductSeller;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/seller/product")
public class ApiProductSeller {

        @Autowired
        Service_ProductSeller service_ProductSeller;

        @GetMapping("/getAll")
        public ApiResponse<PageImpl<Response_ProductListFlashSale>> getAll(
                        @RequestParam(value = "account_id", defaultValue = "1") Integer account_id,
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                        @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
                        @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn,
                        @RequestParam(value = "search", required = false) String search) {
                return ApiResponse.<PageImpl<Response_ProductListFlashSale>>build()
                                .result(service_ProductSeller.getAll(page, size, sortBy, sortColumn, account_id,
                                                search));
        }

        @GetMapping("/edit")
        public ApiResponse<Response_ProductListFlashSale> edit(
                        @RequestParam(value = "product_id", defaultValue = "0") Integer product_id) {
                return ApiResponse.<Response_ProductListFlashSale>build()
                                .result(service_ProductSeller.edit(product_id));
        }

        @PostMapping("/create")
        public ApiResponse<Response_ProductListFlashSale> create(
                        @RequestBody @Valid Request_ProductCreate request_Product) throws IOException {
                return ApiResponse.<Response_ProductListFlashSale>build()
                                .message("Thêm sản phẩm thành công")
                                .result(service_ProductSeller
                                                .create(service_ProductSeller.checkCreate(request_Product)));
        }

        @PostMapping("/update")
        public ApiResponse<Response_ProductListFlashSale> update(
                        @RequestBody @Valid Request_ProductUpdate request_Product) throws IOException {
                return ApiResponse.<Response_ProductListFlashSale>build()
                                .message("Cập nhật sản phẩm thành công")
                                .result(service_ProductSeller
                                                .update(service_ProductSeller.checkUpdate(request_Product)));
        }

        @PostMapping("/create/saveImg")
        public ApiResponse<?> createSaveImage(
                        @RequestParam(value = "product_id", defaultValue = "0") Integer product_id,
                        @RequestPart("imageProducts") List<MultipartFile> images) throws IOException {
                return ApiResponse.build()
                                .message(service_ProductSeller.saveImgCreate(product_id, images)
                                                ? "Thêm Hình Sản Phẩm Thành Công"
                                                : "Thêm Hình Sản Phẩm Thất Bại");

        }

        @PostMapping("/update/saveImg")
        public ApiResponse<?> updateSaveImage(
                        @RequestParam(value = "product_id", defaultValue = "0") Integer product_id,
                        @RequestPart("imageProducts") List<MultipartFile> images) throws IOException {

                return ApiResponse.build()
                                .message(service_ProductSeller.saveImgUpdate(product_id, images)
                                                ? "Cập Nhật Hình Ảnh Thành Công"
                                                : "Cập Nhật Hình Ảnh Thất Bại");
        }

        @DeleteMapping("/delete")
        public ApiResponse<?> delete(@RequestParam("product_id") Integer product_id) {
                service_ProductSeller.delete(product_id);
                return ApiResponse.build()
                                .message("Xóa sản phẩm thành công");
        }

}