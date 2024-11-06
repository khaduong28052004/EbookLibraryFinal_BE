package com.toel.dto.user.response;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.toel.model.Account;
import com.toel.model.Evalue;
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
	   // Chỉ cho phép ánh xạ từ Product sang Response_Product, không trả về trong JSON.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	List<Evalue> evalues;

	double star;
	float quantityEvalue;


}
