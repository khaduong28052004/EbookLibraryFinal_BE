package com.toel.dto.user.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Nationalized;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toel.model.Voucher;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response_Seller {
	Integer id;

	String avatar;

	String fullname;

	boolean gender;

	String background;

	String shopName;

	boolean status;

	Date createAt;

	List<Response_Cart> cart = new ArrayList<Response_Cart>();

	List<Response_Address> addresses;

	List<Voucher> vouchers;

}
