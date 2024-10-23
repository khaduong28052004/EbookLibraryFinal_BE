package com.toel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Integer>{

}
