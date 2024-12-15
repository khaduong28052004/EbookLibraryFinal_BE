package com.toel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.CategoryImage;
import com.toel.model.ImagePlaform;

public interface ImagePlatformsRepository extends JpaRepository<ImagePlaform, Integer> {
    // List<ImagePlaform> findByCategoryImages_id(int categoryImagesId);
    // Tìm kiếm theo categoryImage (đối tượng CategoryImage)
    List<ImagePlaform> findByCategoryImage(CategoryImage categoryImage);

    // Tìm kiếm theo ID của categoryImage
    List<ImagePlaform> findByCategoryImage_Id(Integer categoryImageId);
    List<ImagePlaform> findAllByCategoryImage(CategoryImage categoryImage);

}