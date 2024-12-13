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

import com.toel.dto.admin.request.Role.RequestRoleCreate;
import com.toel.dto.admin.request.Role.RequestRoleUpdate;
import com.toel.dto.admin.response.Response_Role;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.mapper.RoleMapper;
import com.toel.model.Role;
import com.toel.model.RolePermission;
import com.toel.repository.PermissionRepository;
import com.toel.repository.RolePermissionRepository;
import com.toel.repository.RoleRepository;
import com.toel.util.log.LogUtil;

@Service
public class Service_Role {
        @Autowired
        RoleRepository roleRepository;
        @Autowired
        PermissionRepository permissionRepository;
        @Autowired
        RoleMapper roleMapper;
        @Autowired
        RolePermissionRepository rolePermissionRepository;
        @Autowired
        LogUtil service_Log;

        public PageImpl<Response_Role> getRoleNhanVien(String search, Integer page, Integer size,
                        Boolean sortBy,
                        String sortColumn) {
                Pageable pageable = PageRequest.of(page, size,
                                Sort.by(sortBy ? Direction.DESC : Direction.ASC, sortColumn));
                if (search.isEmpty()) {
                        search = null;
                }
                Page<Role> pageRole = roleRepository.selectRoleNhanVien(search, pageable);
                List<Response_Role> list = pageRole.stream()
                                .map(role -> roleMapper.tResponse_Role(role))
                                .collect(Collectors.toList());
                return new PageImpl<>(list, pageable, pageRole.getTotalElements());
        }

        public List<Response_Role> getlistRoleNotNhanVien() {
                List<Response_Role> list = roleRepository.selectRoleNotNhanVien().stream()
                                .map(role -> roleMapper.tResponse_Role(role))
                                .collect(Collectors.toList());
                return list;
        }

        public Response_Role create(RequestRoleCreate requestRoleCreate, Integer accountID) {
                Role entity = roleMapper.createResponse_Role(requestRoleCreate);
                if (!check(entity)) {
                        throw new AppException(ErrorCode.OBJECT_ALREADY_EXISTS, "Tên");
                }
                Role roleNew = roleRepository.save(entity);
                service_Log.setLog(getClass(), accountID, "INFO", "Role", roleMapper.tResponse_Role(roleNew), null,
                                "Thêm quyền");
                return roleMapper
                                .tResponse_Role(roleNew);
        }

        public Response_Role update(RequestRoleUpdate requestRoleUpdate, Integer accountID) {
                Response_Role roleOld = roleMapper.tResponse_Role(roleRepository.findById(requestRoleUpdate.getId())
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Quyền")));
                Role role = roleRepository.findById(requestRoleUpdate.getId())
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Quyền"));
                roleMapper.updatRole(role, requestRoleUpdate);

                if (!check(role)) {
                        throw new AppException(ErrorCode.OBJECT_ALREADY_EXISTS, "Tên");
                }
                service_Log.setLog(getClass(), accountID, "INFO", "Role", roleOld,
                                roleMapper.tResponse_Role(role),
                                "Cập nhật quyền");
                Role roleNew = roleRepository.save(role);

                return roleMapper.tResponse_Role(roleNew);
        }

        public void delete(Integer id, Integer accountID) {
                Role entity = roleRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Quyền"));
                if (!entity.getAccounts().isEmpty()) {
                        throw new AppException(ErrorCode.OBJECT_ACTIVE, "Quyền");
                } else {
                        List<RolePermission> list = rolePermissionRepository.findAllByRole(entity);
                        list.forEach(rlp -> rolePermissionRepository.delete(rlp));
                        roleRepository.delete(entity);
                        service_Log.setLog(getClass(), accountID, "INFO", "Role", roleMapper.tResponse_Role(entity),
                                        null,
                                        "Xóa quyền");
                }
        }

        public Boolean check(Role role) {
                return roleRepository.findAll().stream()
                                .noneMatch(entity -> entity.getName().equalsIgnoreCase(role.getName())
                                                && (entity.getId() != role.getId() || role.getId() == null));
        }
}
