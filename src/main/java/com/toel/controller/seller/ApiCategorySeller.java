package com.toel.controller.seller;

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
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.seller.request.Category.Request_CategoryCreate;
import com.toel.dto.seller.request.Category.Request_CategoryUpdate;
import com.toel.dto.seller.response.Response_Category;
import com.toel.dto.seller.response.Response_CategorySeller;
import com.toel.service.seller.Service_CategorySeller;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/seller/category")
public class ApiCategorySeller {
        @Autowired
        Service_CategorySeller categoryService;

        @GetMapping("/getAll")
        public ApiResponse<PageImpl<Response_Category>> getAll(
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                        @RequestParam(value = "sortBy", defaultValue = "true") Boolean sortBy,
                        @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn,
                        @RequestParam(value = "search", required = false) String search) {
                return ApiResponse.<PageImpl<Response_Category>>build()
                                .result(categoryService.getAll(page, size, sortBy, sortColumn, search));
        }

        @GetMapping("/getAllSeller")
        public ApiResponse<PageImpl<Response_CategorySeller>> getAllSeller(
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                        @RequestParam(value = "sortBy", defaultValue = "true") boolean sortBy,
                        @RequestParam(value = "sortColumn", defaultValue = "id") String sortColumn,
                        @RequestParam(value = "search", required = false) String search,
                        @RequestParam(value = "account_id", defaultValue = "0") Integer account_id) {
                return ApiResponse.<PageImpl<Response_CategorySeller>>build()
                                .result(categoryService.getAllSeller(page, size, sortBy, sortColumn, search,
                                                account_id));
        }

        @GetMapping("/getAllList")
        public ApiResponse<List<Response_Category>> getAllList() {
                return ApiResponse.<List<Response_Category>>build()
                                .result(categoryService.getAllList());
        }

        @GetMapping("/getListByIdParent")
        public ApiResponse<List<Response_Category>> getListByIdParent(
                        @RequestParam(value = "idParent", defaultValue = "0") Integer idParent,
                        @RequestParam(value = "account_id", defaultValue = "0") Integer account_id) {
                return ApiResponse.<List<Response_Category>>build()
                                .result(categoryService.getIdParentAndAccount(idParent, account_id));
        }

        @GetMapping("/edit")
        public ApiResponse<Response_Category> edit(
                        @RequestParam(value = "category_id", defaultValue = "0") Integer category_id) {
                return ApiResponse.<Response_Category>build()
                                .result(categoryService.edit(category_id));
        }

        @PostMapping("/create")
        public ApiResponse<Response_Category> create(
                        @RequestBody @Valid Request_CategoryCreate request_Category) {
                return ApiResponse.<Response_Category>build()
                                .message("Thêm danh mục thành công")
                                .result(categoryService.create(request_Category));
        }

        @PostMapping("/update")
        public ApiResponse<Response_Category> update(
                        @RequestBody @Valid Request_CategoryUpdate request_Category) {
                return ApiResponse.<Response_Category>build()
                                .message("Cập nhật danh mục sản phẩm thành công")
                                .result(categoryService.update(request_Category,null));
        }

        @DeleteMapping("/delete")
        public ApiResponse<?> delete(@RequestParam("id") Integer id_category) {
                categoryService.delete(id_category,null);
                return ApiResponse.build()
                                .message("Xóa thành công danh mục");
        }
}
