package com.toel.dto.user.response;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.toel.model.Account;
import com.toel.model.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response_Product {
	Integer id;
	double price;
	double sale;
	String name;
	String introduce;
	String writerName;
	String publishingCompany;
	Date createAt;
	Integer quantity;
	List<Response_Evalue> evalues;
	Category category;
	double star;
	float quantityEvalue;
	List<Response_ImageProduct> imageProducts;
	Account account;

}
