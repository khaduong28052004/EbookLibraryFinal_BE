package com.toel.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.admin.request.FlashSaleDetail.Resquest_FlashSaleDetailsCreate;
import com.toel.dto.admin.request.FlashSaleDetail.Resquest_FlashSaleDetailsUpdate;
import com.toel.dto.admin.response.Response_FlashSaleDetail;
import com.toel.service.admin.Service_FlashSaleDetail;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/flashsaledetails")
public class ApiFlashSaleDetails {
        @Autowired
        Service_FlashSaleDetail service_FlashSaleDetail;

        @GetMapping
        public ApiResponse<PageImpl<?>> getAllByidFlashSale(
                        @RequestParam(value = "search", required = false) String search,
                        @RequestParam(value = "idFlashSale", required = false) Integer idFlashSale,
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                        @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
                        @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
                return ApiResponse.<PageImpl<?>>build()
                                .result(service_FlashSaleDetail.getAll(search, page, size, sortBy, sortColumn, true,
                                                idFlashSale));
        }

        @GetMapping("notflashsale")
        public ApiResponse<PageImpl<?>> getAllNotFlashSale(
                        @RequestParam(value = "search", required = false) String search,
                        @RequestParam(value = "idFlashSale", required = false) Integer idFlashSale,
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                        @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
                        @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
                return ApiResponse.<PageImpl<?>>build()
                                .result(service_FlashSaleDetail.getAll(search, page, size, sortBy, sortColumn, false,
                                                idFlashSale));
        }

        @PostMapping
        public ApiResponse<Response_FlashSaleDetail> post(@RequestBody @Valid Resquest_FlashSaleDetailsCreate entity) {
                return ApiResponse.<Response_FlashSaleDetail>build()
                                .message("Thêm chi tiết flash sale thành công")
                                .result(service_FlashSaleDetail.create(entity));
        }

        @PutMapping
        public ApiResponse<Response_FlashSaleDetail> put(@RequestBody @Valid Resquest_FlashSaleDetailsUpdate entity) {
                return ApiResponse.<Response_FlashSaleDetail>build()
                                .message("Cập nhật chi tiết flash sale thành công")
                                .result(service_FlashSaleDetail.update(entity));
        }

        @DeleteMapping
        public ApiResponse<Response_FlashSaleDetail> delete(
                        @RequestParam(value = "id", required = false) Integer id) {
                service_FlashSaleDetail.delete(id);
                return ApiResponse.<Response_FlashSaleDetail>build()
                                .message("Xóa chi tiết flashsale thành công");
        }
}
