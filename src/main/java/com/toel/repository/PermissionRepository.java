package com.toel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toel.model.Permission;
import com.toel.model.Role;

import io.lettuce.core.dynamic.annotation.Param;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    Page<Permission> findAllByCotSlugContainingOrDescriptionContaining(String cotSlug, String description,
            Pageable pageable);

    // @Query(value = "SELECT v.permission FROM RolePermission v WHERE v.role= ?1")
    // Page<Permission> findAllByRole(Role role, Pageable pageable);

    // @Query(value = "SELECT v.permission FROM RolePermission v WHERE v.role = ?1
    // AND (v.permission.cotSlug LIKE %?2% OR v.permission.description LIKE %?2%)")
    // Page<Permission> findAllKeyAndByRole(Role role, String key, Pageable
    // pageable);

    @Query("""
                SELECT p FROM Permission p WHERE p.id 
                    NOT IN (SELECT rp.permission.id FROM RolePermission rp WHERE rp.role = :role) 
                    and p.id IN (SELECT rp.permission.id FROM RolePermission rp where rp.role.name like 'ADMINV1') 
                    and (:key iS NULL 
                    OR LOWER(p.cotSlug) LIKE LOWER(CONCAT('%', :key ,'%')) 
                    OR LOWER(p.description) LIKE LOWER(CONCAT('%', :key ,'%')))
            """)
    Page<Permission> findAllPermissionsNotInRole(@Param("key") String key, @Param("role") Role role, Pageable pageable);

    Permission findByCotSlug(String cotSlug);

}
