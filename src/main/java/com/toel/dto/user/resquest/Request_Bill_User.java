package com.toel.dto.user.resquest;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request_Bill_User {
	@NotNull(message = "User ID cannot be null")
	Integer userID;
	String orderStatusFind;
}