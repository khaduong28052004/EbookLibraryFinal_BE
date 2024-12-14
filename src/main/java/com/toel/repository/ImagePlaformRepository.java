package com.toel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.ImagePlaform;
import java.util.List;
import com.toel.model.CategoryImage;

public interface ImagePlaformRepository extends JpaRepository<ImagePlaform, Integer> {
    List<ImagePlaform> findAllByCategoryImage(CategoryImage categoryImage);
}
