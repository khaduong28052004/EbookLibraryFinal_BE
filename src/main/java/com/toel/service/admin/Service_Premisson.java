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

import com.toel.dto.admin.request.Permission.Request_PermissionCreate;
import com.toel.dto.admin.request.Permission.Requset_PermissionUpdate;
import com.toel.dto.admin.request.RolePermission.Request_RolePermissionCreate;
import com.toel.dto.admin.response.Response_Permission;
import com.toel.dto.admin.response.Response_Permission_Role;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.admin.PermissionMapper;
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
    RolePermissionRepository rolePermissionRepository;
    @Autowired
    Service_RolePermission service_RolePermission;

    public PageImpl<Response_Permission> getAll(
            String search, String role, Integer page, Integer size, Boolean sortBy, String sortColumn) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
        Page<Permission> pagePermission = null;
        Role roleEntity = (role == null) ? null : roleRepository.findByNameIgnoreCase(role);
        if (search == null && role == null) {
            pagePermission = permissionRepository.findAll(pageable);
        } else if (search == null) {
            pagePermission = permissionRepository.findAllByRole(roleEntity, pageable);
        } else if (role == null) {
            pagePermission = permissionRepository.findAllByCotSlugContainingOrDescriptionContaining(search, search,
                    pageable);
        } else {
            pagePermission = permissionRepository.findAllKeyAndByRole(roleEntity, search, pageable);
        }
        List<Response_Permission> list = pagePermission.stream()
                .map(permissionRepository -> permissionMapper.toPermission(permissionRepository))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pagePermission.getTotalElements());
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

    public PageImpl<Response_Permission> getAllNotRole(Integer idrole, Integer page, Integer size, Boolean sortBy,
            String sortColumn) {
        Role role = roleRepository.findById(idrole)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Role"));
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
        Page<Permission> pagePermission = permissionRepository.findAllPermissionsNotInRole(role, pageable);
        List<Response_Permission> list = pagePermission.stream()
                .map(permissionRepository -> permissionMapper.toPermission(permissionRepository))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pagePermission.getTotalElements());
    }

    public Response_Permission create(Request_PermissionCreate entity) {
        Role role = roleRepository.findById(entity.getRole())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Role"));
        Permission permission = permissionRepository.save(permissionMapper.toPermissionCreate(entity));
        Request_RolePermissionCreate rolePermission = new Request_RolePermissionCreate();
        rolePermission.setPermission(permission.getId());
        rolePermission.setRole(entity.getRole());
        service_RolePermission.create(rolePermission);
        return permissionMapper.toPermission(permission);
    }

    public Response_Permission update(Requset_PermissionUpdate entity) {
        Permission permission = permissionRepository.findById(entity.getId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Permission"));
        permissionMapper.toPermissionUpdate(permission, entity);
        return permissionMapper.toPermission(permission);
    }

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
