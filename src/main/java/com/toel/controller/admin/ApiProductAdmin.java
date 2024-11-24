package com.toel.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.admin.response.Response_ProductListFlashSale;
import com.toel.service.admin.Service_Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/product")
public class ApiProductAdmin {
        @Autowired
        Service_Product service_Product;

        @GetMapping
        public ApiResponse<PageImpl<Response_ProductListFlashSale>> getAll(
                        @RequestParam(value = "search", required = false) String search,
                        @RequestParam(value = "option", defaultValue = "tatca") String option,
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                        @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
                        @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
                return ApiResponse.<PageImpl<Response_ProductListFlashSale>>build()
                                .result(service_Product.getAll(page, size, sortBy, sortColumn, search, option));
        }

        @PutMapping
        public ApiResponse<Response_ProductListFlashSale> putStatus(
                        @RequestParam(value = "id", required = false) Integer id) {
                Response_ProductListFlashSale entity = service_Product.updateStatus(id);
                return ApiResponse.<Response_ProductListFlashSale>build()
                                .message(!entity.isDelete() ? "Khôi phục hoạt động thành công"
                                                : "Ngừng hoạt động thành công")
                                .result(entity);
        }

        @PutMapping("browse")
        public ApiResponse<Response_ProductListFlashSale> putActive(
                        @RequestParam(value = "id", required = false) Integer id,
                        @RequestParam(value = "status", required = false) Boolean status) {
                return ApiResponse.<Response_ProductListFlashSale>build()
                                .message(status ? "Duyệt thành công" : "Hủy thành công")
                                .result(service_Product.updateActive(id, status));
        }
}
