package com.toel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toel.model.Role;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByNameIgnoreCase(String name);

    @Query("SELECT v FROM Role v JOIN v.rolePermissions rp WHERE rp.permission.id = ?1")
    List<Role> selectAllByPermission(Integer id);

}
