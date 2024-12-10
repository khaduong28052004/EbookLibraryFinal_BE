package com.toel.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.user.PlatformDTO;
import com.toel.model.Platform;
import com.toel.service.user.PlatformService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
public class ApiSlideShow {
        @Autowired
    private PlatformService platformService;

    // // API cho Admin: Lấy tất cả thông tin đầy đủ
    // @GetMapping("/admin/platforms")
    // public ResponseEntity<?> getAllPlatforms() {
    //     List<Platform> platforms = platformService.getAllPlatforms();
    //     return ResponseEntity.ok(platforms);
    // }

    // API cho User: Lấy thông tin nền tảng với danh mục và hình ảnh
    @GetMapping("/user/platforms")
    public ResponseEntity<?> getPlatformsByCategoryImages(@RequestParam("categoryImagesId") Integer categoryImagesId) {
        List<PlatformDTO> platformDTOs = platformService.getPlatformsByCategory(categoryImagesId);
        return ResponseEntity.ok(platformDTOs);
    }
}
