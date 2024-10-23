package com.toel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.RolePermission;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer> {

}
