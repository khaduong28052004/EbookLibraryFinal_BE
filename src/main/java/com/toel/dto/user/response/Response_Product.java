package com.toel.dto.user.response;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.toel.model.Account;
import com.toel.model.BillDetail;
import com.toel.model.Category;
import com.toel.model.FlashSaleDetail;

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
	@JsonIgnore
	List<BillDetail> billDetails;
	Integer sold;
	Category category;
	double star;
	float quantityEvalue;
	List<Response_ImageProduct> imageProducts;
	Account account;
	double weight;
	FlashSaleDetail flashSaleDetail = null;

}
