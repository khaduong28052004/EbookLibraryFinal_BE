package com.toel.dto.user.resquest;

import java.util.Date;

import org.mapstruct.Mapper;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Mapper(componentModel = "spring")
public class Request_Bill {
	@NotNull(message = "User ID cannot be null")
	Integer userID;
	String orderStatusFind;
}
