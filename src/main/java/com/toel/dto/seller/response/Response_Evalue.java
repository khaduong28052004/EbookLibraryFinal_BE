package com.toel.dto.seller.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_Evalue {
	private Integer id;
	private Integer star;
	private String content;
	private Integer idParent;
	private Response_Account account;
	private Response_Product product;
	private Response_BillDetail billDetail;
	private List<Response_ImageEvalue> imageEvalues;
}
