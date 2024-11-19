package com.toel.dto.user.response;

import java.util.Date;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response_User {
	Integer id;

	String avatar;

	String fullname;

	boolean gender;

	String email;

	Date birthday;

	String phone;

	boolean status;

	Date createAt;

	List<Response_Address> addresses;

}
