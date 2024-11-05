package com.toel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toel.model.Permission;
import com.toel.model.Role;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    Page<Permission> findAllByCotSlugContainingOrDescriptionContaining(String cotSlug, String description,
            Pageable pageable);

    @Query(value = "SELECT v.permission FROM RolePermission v WHERE v.role= ?1")
    Page<Permission> findAllByRole(Role role, Pageable pageable);

    @Query(value = "SELECT v.permission FROM RolePermission v WHERE v.role = ?1 AND (v.permission.cotSlug LIKE %?2% OR v.permission.description LIKE %?2%)")
    Page<Permission> findAllKeyAndByRole(Role role, String key, Pageable pageable);

    @Query("SELECT p FROM Permission p WHERE p.id NOT IN (SELECT rp.permission.id FROM RolePermission rp WHERE rp.role = ?1)")
    Page<Permission> findAllPermissionsNotInRole(Role role, Pageable pageable);
}
