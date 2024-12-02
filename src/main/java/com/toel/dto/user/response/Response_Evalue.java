package com.toel.dto.user.response;

import java.util.Date;
import java.util.List;

import com.toel.model.Account;
import com.toel.model.ImageEvalue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_Evalue {
	Integer id;

	int star;

	Integer idParent;

	Date createAt;

	String content;

	List<Response_ImageEvalue> imageEvalues;

	Account account;

}
