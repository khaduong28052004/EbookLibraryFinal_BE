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
import com.toel.dto.seller.request.Voucher.Request_VoucherCreate;
import com.toel.dto.seller.request.Voucher.Request_VoucherUpdate;
import com.toel.dto.seller.response.Response_Voucher;
import com.toel.dto.seller.response.Response_VoucherDetail;
import com.toel.service.seller.Service_VoucherSeller;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/voucher")
public class ApiVoucherSeller {

        @Autowired
        Service_VoucherSeller service_Voucher;

        @GetMapping("/getAll")
        public ApiResponse<PageImpl<Response_Voucher>> getAll(
                        @RequestParam(value = "account_id", defaultValue = "0") Integer account_id,
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                        @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
                        @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn,
                        @RequestParam(value = "search", required = false) String search) {
                return ApiResponse.<PageImpl<Response_Voucher>>build()
                                .result(service_Voucher.getAll(page, size, sortBy, sortColumn, account_id, search));
        }

        @PostMapping("/create")
        public ApiResponse<Response_Voucher> create(
                        @RequestBody @Valid Request_VoucherCreate request_Voucher) {
                return ApiResponse.<Response_Voucher>build()
                                .message("Thêm voucher thành công")
                                .result(service_Voucher.create(request_Voucher));
        }

        @PostMapping("/update")
        public ApiResponse<Response_Voucher> update(
                        @RequestBody @Valid Request_VoucherUpdate request_Voucher) {
                return ApiResponse.<Response_Voucher>build()
                                .message("Cập nhật voucher thành công")
                                .result(service_Voucher.update(request_Voucher));
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
                return ApiResponse.build()
                                .message(service_Voucher.delete(voucher_id) ? "Khôi phục hoạt động thành công"
                                                : "Ngừng hoạt động thành công");
        }

        @GetMapping("/getDetail")
        public ApiResponse<PageImpl<Response_VoucherDetail>> getDetail(
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                        @RequestParam(value = "sortBy", defaultValue = "true") boolean sortBy,
                        @RequestParam(value = "sortColum", defaultValue = "id") String sortColum,
                        @RequestParam(value = "voucher_id", defaultValue = "0") Integer voucher_id) {
                return ApiResponse.<PageImpl<Response_VoucherDetail>>build()
                                .result(service_Voucher.getAllDetail(page, size, sortBy, sortColum, voucher_id));
        }
}
