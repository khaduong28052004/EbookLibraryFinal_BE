package com.toel.dto.seller.response;

import java.util.Date;

import com.toel.model.Account;
import com.toel.model.Product;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_Like {
	Integer id;
	boolean status;
	Date createAt;
	Integer product;
	Integer account;
}
