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

import com.toel.dto.admin.response.Response_Role;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.RoleMapper;
import com.toel.model.Permission;
import com.toel.model.Role;
import com.toel.repository.PermissionRepository;
import com.toel.repository.RoleRepository;

@Service
public class Service_Role {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PermissionRepository permissionRepository;
    @Autowired
    RoleMapper roleMapper;

    public PageImpl<Response_Role> getRoleNotPermissonRole(Integer idPermission, Integer page, Integer size, Boolean sortBy,
            String sortColumn) {
        Permission entity = permissionRepository.findById(idPermission)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Permission"));
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
        Page<Role> pageRole = roleRepository.findAllPermissionsNotInRole(entity, pageable);
        List<Response_Role> list = pageRole.stream()
                .map(role -> roleMapper.tResponse_Role(role))
                .collect(Collectors.toList());
                return new PageImpl<>(list,pageable,pageRole.getTotalElements());
    }

}
