package com.toel.dto.user.resquest;

import java.util.Date;

import org.mapstruct.Mapper;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Mapper(componentModel = "spring")
public class Request_Bill_User {
	@NotNull(message = "User ID cannot be null")
	Integer userID;
	String orderStatusFind;
}
