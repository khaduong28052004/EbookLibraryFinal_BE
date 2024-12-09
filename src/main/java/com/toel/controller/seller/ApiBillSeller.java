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
import com.toel.dto.seller.request.Request_Bill;
import com.toel.dto.seller.response.Response_Bill;
import com.toel.service.seller.Service_BillSeller;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/seller/bill")
public class ApiBillSeller {

        @Autowired
        Service_BillSeller service_Bill;

        @GetMapping("/getAll")
        public ApiResponse<PageImpl<Response_Bill>> getAll(
                        @RequestParam(value = "account_id", defaultValue = "0") Integer account_id,
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                        @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
                        @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn,
                        @RequestParam(value = "search", required = false) String search) {
                return ApiResponse.<PageImpl<Response_Bill>>build()
                                .result(service_Bill.getAll(page, size, sortBy, sortColumn, account_id, search));
        }

        @PostMapping("updateOrderStatus")
        public ApiResponse<Response_Bill> updateOrderStatus(
                        @RequestBody @Valid Request_Bill request_Bill) {
                return ApiResponse.<Response_Bill>build()
                                .message("Cập nhật trạng thái đơn hàng thành công")
                                .result(service_Bill.updateOrderStatus(request_Bill));
        }

        @PostMapping("huy")
        public ApiResponse<Response_Bill> huy(
                        @RequestParam(value = "content", defaultValue = "") String content,
                        @RequestBody @Valid Request_Bill request_Bill) {
                return ApiResponse.<Response_Bill>build()
                                .message("Hủy đơn hàng thành công")
                                .result(service_Bill.huy(content, request_Bill));
        }

}
