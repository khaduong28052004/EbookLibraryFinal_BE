package com.toel.dto.user.response;

import java.util.Date;

import org.hibernate.annotations.Nationalized;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

public class Response_Account {
	Integer id;

	String username;

	String avatar;

	String fullname;

	boolean gender;



	@Temporal(TemporalType.DATE)
	// @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	Date birthday;

	String phone;

	String background;


	String shopName;






	boolean status;

	Date createAt;

	Date createAtSeller;

}
