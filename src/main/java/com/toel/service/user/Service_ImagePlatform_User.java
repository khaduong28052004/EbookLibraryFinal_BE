package com.toel.service.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toel.dto.user.CategoryImageDTO;
import com.toel.dto.user.ImagePlatformDTO;
import com.toel.model.CategoryImage;
import com.toel.model.ImagePlaform;
import com.toel.repository.CategoryImagesRepository;
import com.toel.repository.ImagePlatformsRepository;
@Service
public class Service_ImagePlatform_User {
   
    @Autowired
    private CategoryImagesRepository imagePlatformRepository;
    public CategoryImageDTO getCategoryImageWithImages(Integer categoryImageId) {
        Optional<CategoryImage> optionalCategoryImage = imagePlatformRepository.findById(categoryImageId);
        if (!optionalCategoryImage.isPresent()) {
            return null; // hoặc trả về một DTO mặc định
        }
        CategoryImage categoryImage = optionalCategoryImage.get();
        List<ImagePlatformDTO> images = categoryImage.getImagePlaforms().stream()
                .map(image -> new ImagePlatformDTO(image.getId(), image.getUrl()))
                .collect(Collectors.toList());
        return new CategoryImageDTO(categoryImage.getId(), categoryImage.getName(), images);
    }
}
