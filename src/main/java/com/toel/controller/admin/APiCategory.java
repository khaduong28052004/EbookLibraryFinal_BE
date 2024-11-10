package com.toel.controller.admin;

import java.util.List;

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
import com.toel.dto.seller.request.Category.Request_CategoryCreate;
import com.toel.dto.seller.request.Category.Request_CategoryUpdate;
import com.toel.dto.seller.response.Response_Category;
import com.toel.service.seller.Service_CategorySeller;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/category")
public class APiCategory {
    @Autowired
    Service_CategorySeller service_CategorySeller;

    @GetMapping
    public ApiResponse<PageImpl<Response_Category>> getAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
            @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn,
            @RequestParam(value = "search", required = false) String search) {
        return ApiResponse.<PageImpl<Response_Category>>build()
                .result(service_CategorySeller.getAll(page, size, sortBy, sortColumn, search));
    }

    @GetMapping("/getListByIdParent")
    public ApiResponse<List<Response_Category>> getListByIdParent(
            @RequestParam(value = "idParent", defaultValue = "0") Integer idParent) {
        return ApiResponse.<List<Response_Category>>build()
                .result(service_CategorySeller.getIdParent(idParent));
    }

    @PostMapping
    public ApiResponse<Response_Category> post(
            @RequestBody @Valid Request_CategoryCreate request_Category) {
        return ApiResponse.<Response_Category>build()
                .message("Thêm thể loại thành công")
                .result(service_CategorySeller.create(request_Category));
    }

    @PutMapping
    public ApiResponse<Response_Category> put(
            @RequestBody @Valid Request_CategoryUpdate request_Category) {
        return ApiResponse.<Response_Category>build()
                .message("Cập nhật thể loại sản phẩm thành công")
                .result(service_CategorySeller.update(request_Category));
    }

    @DeleteMapping
    public ApiResponse<Response_Category> delete(@RequestParam(value = "id", required = false) Integer id) {
        service_CategorySeller.delete(id);
        return ApiResponse.<Response_Category>build()
                .message("Xóa thành công");
    }
}
