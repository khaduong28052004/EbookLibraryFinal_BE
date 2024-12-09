package com.toel.service.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.toel.dto.admin.response.Response_Permission;
import com.toel.dto.admin.response.Response_Permission_Role;
import com.toel.dto.admin.response.Response_RolePermission;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.PermissionMapper;
import com.toel.mapper.RolePermissionMapper;
import com.toel.model.Permission;
import com.toel.model.Role;
import com.toel.model.RolePermission;
import com.toel.repository.AccountRepository;
import com.toel.repository.PermissionRepository;
import com.toel.repository.RolePermissionRepository;
import com.toel.repository.RoleRepository;

@Service
public class Service_Premisson {
        @Autowired
        AccountRepository accountRepository;
        @Autowired
        PermissionRepository permissionRepository;
        @Autowired
        RoleRepository roleRepository;
        @Autowired
        PermissionMapper permissionMapper;
        @Autowired
        RolePermissionMapper rolePermissionmapper;
        @Autowired
        RolePermissionRepository rolePermissionRepository;
        @Autowired
        Service_RolePermission service_RolePermission;

        public PageImpl<?> getAll(
                        String search, String role, Integer page, Integer size, Boolean sortBy, String sortColumn) {

                Pageable pageable = PageRequest.of(page, size,
                                Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));

                Role roleEntity = (role == null) ? null : roleRepository.findByNameIgnoreCase(role);

                if (search == null && role == null) {
                        Page<Permission> pagePermission = permissionRepository.findAll(pageable);
                        List<Response_Permission> list = pagePermission.stream()
                                        .map(permissionMapper::toPermission)
                                        .collect(Collectors.toList());
                        return new PageImpl<>(list, pageable, pagePermission.getTotalElements());
                }

                if (search == null) {
                        Page<RolePermission> pageRolePermission = rolePermissionRepository.findAllByRole(roleEntity,
                                        pageable);
                        List<Response_RolePermission> list = pageRolePermission.stream()
                                        .map(rolePermissionmapper::toRolePermission)
                                        .collect(Collectors.toList());
                        return new PageImpl<>(list, pageable, pageRolePermission.getTotalElements());
                }

                if (role == null) {
                        Page<Permission> pagePermission = permissionRepository
                                        .findAllByCotSlugContainingOrDescriptionContaining(search, search, pageable);
                        List<Response_Permission> list = pagePermission.stream()
                                        .map(permissionMapper::toPermission)
                                        .collect(Collectors.toList());
                        return new PageImpl<>(list, pageable, pagePermission.getTotalElements());
                }

                Page<RolePermission> pageRolePermission = rolePermissionRepository
                                .findAllKeyAndByRole(roleEntity, search, pageable);
                List<Response_RolePermission> list = pageRolePermission.stream()
                                .map(rolePermissionmapper::toRolePermission)
                                .collect(Collectors.toList());
                return new PageImpl<>(list, pageable, pageRolePermission.getTotalElements());
        }

        public Response_Permission_Role getByID(Integer id) {
                Permission entity = permissionRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Permission"));
                List<Role> list = roleRepository.selectAllByPermission(id);
                Response_Permission_Role permission_Role = new Response_Permission_Role();
                permission_Role.setCotSlug(entity.getCotSlug());
                permission_Role.setDescription(entity.getDescription());
                permission_Role.setId(entity.getId());
                permission_Role.setRolePermissions(list);
                return permission_Role;
        }

        public PageImpl<Response_Permission> getAllNotRole(String search, String Namerole, Integer page, Integer size, Boolean sortBy,
                        String sortColumn) {
                Role role = roleRepository.findByNameIgnoreCase(Namerole);
                Pageable pageable = PageRequest.of(page, size,
                                Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
                Page<Permission> pagePermission = permissionRepository.findAllPermissionsNotInRole(search, role, pageable);
                List<Response_Permission> list = pagePermission.stream()
                                .map(permissionRepository -> permissionMapper.toPermission(permissionRepository))
                                .collect(Collectors.toList());
                return new PageImpl<>(list, pageable, pagePermission.getTotalElements());
        }

        // public Response_Permission create(Request_PermissionCreate entity) {
        // roleRepository.findById(entity.getRole())
        // .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Role"));
        // Permission permission =
        // permissionRepository.save(permissionMapper.toPermissionCreate(entity));
        // Request_RolePermissionCreate rolePermission = new
        // Request_RolePermissionCreate();
        // rolePermission.setPermission(permission.getId());
        // rolePermission.setRole(entity.getRole());
        // service_RolePermission.create(rolePermission);
        // return permissionMapper.toPermission(permissionRepository.save(permission));
        // }

        // public Response_Permission update(Requset_PermissionUpdate entity) {
        // Permission permission = permissionRepository.findById(entity.getId())
        // .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND,
        // "Permission"));
        // permissionMapper.toPermissionUpdate(permission, entity);
        // return permissionMapper.toPermission(permissionRepository.save(permission));
        // }

        public void delete(Integer id) {
                Permission entity = permissionRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Permission"));
                List<RolePermission> rolePermission = rolePermissionRepository.findAllByPermission(entity);
                if (rolePermission.size() == 0) {
                        permissionRepository.delete(entity);
                } else {
                        throw new AppException(ErrorCode.OBJECT_ACTIVE, "Permission");
                }
        }
}
