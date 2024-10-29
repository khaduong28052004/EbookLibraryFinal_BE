package com.toel.dto.user.response;

import java.util.Date;
import java.util.List;

import org.mapstruct.Mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Mapper(componentModel = "spring")
public class Response_Bill_Shop_User {
	 Integer shopId; 
	 String shopName; 
	 String shopAvatar;
	 List<Response_Bill_User> bills;
}
