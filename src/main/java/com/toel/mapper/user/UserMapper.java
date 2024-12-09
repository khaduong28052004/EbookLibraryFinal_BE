package com.toel.mapper.user;

import org.mapstruct.Mapper;

import com.toel.dto.user.response.Response_User;
import com.toel.model.Account;

@Mapper
public interface UserMapper {
	Response_User UserMapperToAccount(Account account);
	
}
