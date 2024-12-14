package com.toel.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.user.CategoryImageDTO;
import com.toel.dto.user.ImagePlatformDTO;
import com.toel.service.user.Service_ImagePlatform_User;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
public class ApiSlideShow {
    @Autowired
    private Service_ImagePlatform_User imagePlatformService;

    @GetMapping("/user/platforms/{id}")
    public ResponseEntity<CategoryImageDTO> getCategoryImageWithImages(@PathVariable Integer id) {
        CategoryImageDTO categoryImageDTO = imagePlatformService.getCategoryImageWithImages(id);
        if (categoryImageDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Trả về 404 nếu không tìm thấy
        }
        return ResponseEntity.ok(categoryImageDTO);
    }
}
