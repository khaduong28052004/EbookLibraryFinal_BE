package com.toel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.CategoryImage;
import com.toel.model.Platform;

// Repository cho bảng CategoryImages
public interface CategoryImagesRepository extends JpaRepository<CategoryImage, Integer> {
    List<CategoryImage> findByPlatformId(int platformId);
}

