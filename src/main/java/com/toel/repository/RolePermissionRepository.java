package com.toel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.Role;
import com.toel.model.RolePermission;
import java.util.List;
import com.toel.model.Permission;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer> {

    List<RolePermission> findAllByPermission(Permission permission);

    List<RolePermission> findByRole(Role role);
}
