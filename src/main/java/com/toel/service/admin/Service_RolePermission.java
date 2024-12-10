package com.toel.service.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toel.dto.admin.request.RolePermission.Request_RolePermissionCreate;
import com.toel.dto.admin.response.Response_RolePermission;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.RolePermissionMapper;
import com.toel.model.Permission;
import com.toel.model.Role;
import com.toel.model.RolePermission;
import com.toel.repository.PermissionRepository;
import com.toel.repository.RolePermissionRepository;
import com.toel.repository.RoleRepository;
import com.toel.service.Service_Log;

@Service
public class Service_RolePermission {
    @Autowired
    PermissionRepository permissionRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    RolePermissionRepository rolePermissionRepository;
    @Autowired
    RolePermissionMapper rolePermissionMapper;
    @Autowired
    Service_Log service_Log;

    public List<Response_RolePermission> create(Request_RolePermissionCreate entity, Integer accountID) {
        Role role = roleRepository.findByNameIgnoreCase(entity.getRole());
        List<Response_RolePermission> list = new ArrayList<>();
        entity.getPermission().forEach(permissionId -> {
            RolePermission rolePermission = new RolePermission();
            Permission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Permission"));
            rolePermission.setPermission(permission);
            rolePermission.setRole(role);
            RolePermission rolePermissionNew =rolePermissionRepository.save(rolePermission);
            service_Log.setLog(getClass(), accountID, "INFO", "ROLEPERMISSION", rolePermissionNew.getId(), "Thêm tiết quyền");
            list.add(rolePermissionMapper.toRolePermission(rolePermissionNew));
        });

        return list;
    }

    public void delete(Integer id, Integer accountID) {
        RolePermission rolePermission = rolePermissionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Permission"));
        rolePermissionRepository.delete(rolePermission);
        service_Log.setLog(getClass(), accountID, "INFO", "ROLEPERMISSION", id, "Xóa chi tiết quyền");
    }
}
