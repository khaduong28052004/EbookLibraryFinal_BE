package com.toel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.Role;
import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByNameIgnoreCase(String name);
}
