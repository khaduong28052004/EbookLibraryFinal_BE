package com.toel.dto.user.response;

import org.hibernate.annotations.Nationalized;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_Address {
	Integer id;

	boolean status;

	String phone;

	String fullNameAddress;

	Integer province;

	Integer district;

	String wardCode;
}
