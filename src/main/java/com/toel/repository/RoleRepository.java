package com.toel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.toel.model.Permission;
import com.toel.model.Role;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {
  Role findByNameIgnoreCase(String name);

  @Query("SELECT v FROM Role v JOIN v.rolePermissions rp WHERE rp.permission.id = ?1")
  List<Role> selectAllByPermission(Integer id);

  @Query("SELECT p FROM Role p WHERE p.id NOT IN (SELECT rp.role.id FROM RolePermission rp WHERE rp.permission = ?1)")
  Page<Role> findAllPermissionsNotInRole(Permission permission, Pageable pageable);

  @Query("SELECT v FROM Role v WHERE v.name != 'ADMIN' and v.name != 'SELLER' and v.name != 'USER'")
  List<Role> selectRoleNhanVien();

  @Query("""
          SELECT v
          FROM Role v
          LEFT JOIN v.accounts a
          WHERE v.name NOT IN ('ADMIN', 'SELLER', 'USER','ADMINV1')
            AND (:key IS NULL
                 OR :key LIKE CONCAT('%', v.name, '%')
                 OR :key LIKE CONCAT('%', v.description, '%')
                 OR :key LIKE CONCAT('%', a.fullname, '%'))
      """)
  Page<Role> selectRoleNhanVien(@Param("key") String key, Pageable pageable);

  @Query("SELECT v FROM Role v WHERE v.name NOT IN ('ADMIN', 'SELLER', 'USER','ADMINV1') and SIZE(v.accounts) = 0")
  List<Role> selectRoleNotNhanVien();

}
