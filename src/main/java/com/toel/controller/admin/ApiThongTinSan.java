package com.toel.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.admin.request.Platform.Request_PlatformUpdate;
import com.toel.dto.admin.request.Platform.Request_PlatformUpdatePolicies;
import com.toel.dto.admin.response.Response_Platform;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.model.CategoryImage;
import com.toel.repository.CategoryImagesRepository;
import com.toel.service.admin.Service_ThongTinSan;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/thongtinsan")
public class ApiThongTinSan {
    @Autowired
    Service_ThongTinSan san;
    @Autowired
    CategoryImagesRepository categoryImageRepository;

    @GetMapping
    public ApiResponse<Response_Platform> getThongTinSan() {
        return ApiResponse.<Response_Platform>build()
                .result(san.getIdPlatform(1));
    }

    @PutMapping("thongtinchung")
    public ApiResponse<Response_Platform> putThongTinSan(
            @Valid @RequestBody Request_PlatformUpdate request_PlatformUpdate) {
        return ApiResponse.<Response_Platform>build()
                .result(san.update(request_PlatformUpdate));
    }
    @PutMapping("chinhsach")
    public ApiResponse<Response_Platform> putChinhsach(
            @Valid @RequestBody Request_PlatformUpdatePolicies request_PlatformUpdatePoliciese) {
        return ApiResponse.<Response_Platform>build()
                .result(san.updatePolicies(request_PlatformUpdatePoliciese));
    }

    @PostMapping("chude/saveImg")
    public ApiResponse<?> postChuDe(
            @RequestParam(value = "id", defaultValue = "1") Integer id,
            @RequestPart("images") List<MultipartFile> images) {
        CategoryImage categoryImage = categoryImageRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Loại Ảnh"));
        boolean status = san.updateProductImages(categoryImage, images);
        return ApiResponse.build()
                .message(status ? "Cập nhật thành công" : "Cập nhật thất bại    ");
    }

}
