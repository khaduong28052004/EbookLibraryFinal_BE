package com.toel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toel.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

        @Query("SELECT c FROM Category c WHERE c.idParent = ?1 AND c.account.id = ?2")
        List<Category> findALlByIdParentAndAccount(Integer idParent, Integer accountId);

        @Query("SELECT c FROM Category c WHERE c.idParent = ?1")
        List<Category> findALlByIdParent(Integer idParent);

        @Query("SELECT c FROM Category c WHERE  c.account.id = ?1")
        List<Category> findALlByIdAccount(Integer accountId);

        @Query("SELECT c FROM Category c WHERE c.idParent = 0")
        List<Category> findALlByIdParentZero();

        @Query("SELECT c FROM Category c WHERE c.idParent = 0 AND " +
                        "(?1 IS NULL OR c.name LIKE CONCAT('%', ?1, '%'))")
        Page<Category> findALlBySearch(String search, Pageable pageable);

        @Query("SELECT c.id AS id, c.name AS name, c.idParent, parent.name AS parentName, " +
                        "CASE WHEN COUNT(p) > 0 THEN true ELSE false END AS hasProducts " +
                        "FROM Category c LEFT JOIN Category parent ON c.idParent = parent.id " +
                        "LEFT JOIN Product p ON p.category.id = c.id " +
                        "WHERE c.idParent != 0 " +
                        "AND c.account.id = ?2" +
                        "AND (?1 IS NULL OR c.name LIKE CONCAT('%', ?1, '%')) " +
                        "GROUP BY c.id, parent.name")
        Page<Object[]> findCategoriesWithParentName(String search, Integer accountId, Pageable pageable);

}
