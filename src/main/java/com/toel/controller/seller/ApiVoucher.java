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
import com.toel.dto.seller.request.Request_Voucher;
import com.toel.dto.seller.response.Response_Voucher;
import com.toel.service.seller.Service_Voucher;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/seller/voucher")
public class ApiVoucher {
        @Autowired
        Service_Voucher service_Voucher;

        @GetMapping("/getAll")
        public ApiResponse<PageImpl<Response_Voucher>> getAll(
                        @RequestParam(value = "account_id", defaultValue = "0") Integer account_id,
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                        @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
                        @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
                return ApiResponse.<PageImpl<Response_Voucher>>build()
                                .result(service_Voucher.getAll(page, size, sortBy, sortColumn, account_id));
        }

        @PostMapping("/save")
        public ApiResponse<Response_Voucher> save(
                        @RequestBody @Valid Request_Voucher request_Voucher,
                        @RequestParam("account_id") Integer account_id) {
                Response_Voucher response_Voucher = service_Voucher.save(request_Voucher, account_id);
                return ApiResponse.<Response_Voucher>build()
                                .message(response_Voucher.getId() == null ? "Thêm voucher thành công"
                                                : "Cập nhật voucher thành công")
                                .result(response_Voucher);
        }

        @GetMapping("edit")
        public ApiResponse<Response_Voucher> get(
                        @RequestParam("voucher_id") Integer voucher_id) {
                return ApiResponse.<Response_Voucher>build()
                                .result(service_Voucher.edit(voucher_id));
        }

        @DeleteMapping("/delete")
        public ApiResponse delete(
                        @RequestParam("voucher_id") Integer voucher_id) {
                boolean status = service_Voucher.delete(voucher_id);
                return ApiResponse.build()
                                .message(status ? "Khôi phục hoạt động thành công" : "Ngừng hoạt động thành công");
        }
}
