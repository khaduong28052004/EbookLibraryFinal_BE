package com.toel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toel.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("SELECT c FROM Category c WHERE c.idParent = ?1")
    List<Category> findALlByIdParent(Integer idParent);

    @Query("SELECT c FROM Category c WHERE c.idParent = 0")
    List<Category> findALlByIdParentZero();

    @Query("SELECT c FROM Category c WHERE c.idParent = 0 AND " +
            "(?1 IS NULL OR c.name LIKE CONCAT('%', ?1, '%'))")
    Page<Category> findALlBySearch(String search, Pageable pageable);

}
