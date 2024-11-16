package com.toel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toel.model.RolePermission;
import java.util.List;
import com.toel.model.Permission;
import com.toel.model.Role;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer> {
    List<RolePermission> findAllByPermission(Permission permission);

    List<RolePermission> findByRole(Role role);

    @Query(value = "SELECT v FROM RolePermission v WHERE v.role= ?1")
    Page<RolePermission> findAllByRole(Role role, Pageable pageable);

    @Query(value = "SELECT v FROM RolePermission v WHERE v.role = ?1 AND (v.permission.cotSlug LIKE %?2% OR v.permission.description LIKE %?2%)")
    Page<RolePermission> findAllKeyAndByRole(Role role, String key, Pageable pageable);

}
