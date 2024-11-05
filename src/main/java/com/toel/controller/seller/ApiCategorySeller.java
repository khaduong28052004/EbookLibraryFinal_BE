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
import com.toel.dto.seller.request.Category.Request_CategoryCreate;
import com.toel.dto.seller.request.Category.Request_CategoryUpdate;
import com.toel.dto.seller.response.Response_Category;
import com.toel.service.seller.Service_CategorySeller;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/category")
public class ApiCategorySeller {

    @Autowired
    Service_CategorySeller categoryService;

    @GetMapping("/getAll")
    public ApiResponse<PageImpl<Response_Category>> getAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
            @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn) {
        return ApiResponse.<PageImpl<Response_Category>>build()
                .result(categoryService.getAll(page, size, sortBy, sortColumn));
    }

    @PostMapping("/create")
    public ApiResponse<Response_Category> create(
            @RequestBody @Valid Request_CategoryCreate request_Category) {
        return ApiResponse.<Response_Category>build()
                .message("Thêm thể loại thành công")
                .result(categoryService.create(request_Category));
    }

    @PostMapping("/update")
    public ApiResponse<Response_Category> update(
            @RequestBody @Valid Request_CategoryUpdate request_Category) {
        return ApiResponse.<Response_Category>build()
                .message("Cập nhật thể loại sản phẩm thành công")
                .result(categoryService.update(request_Category));
    }

    @DeleteMapping("/delete")
    public ApiResponse delete(@RequestParam("id") Integer id_category) {
        categoryService.delete(id_category);
        return ApiResponse.build()
                .message("Xóa thành công thể loại");
    }
}
