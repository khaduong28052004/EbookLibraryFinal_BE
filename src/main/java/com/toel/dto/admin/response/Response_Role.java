package com.toel.dto.admin.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_Role {
	Integer id;
	String name;
	String description;
	// List<Response_RolePermission> rolePermissions;
	// List<Response_Account> accounts;
}
