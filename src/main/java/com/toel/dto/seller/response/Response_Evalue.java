package com.toel.dto.seller.response;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_Evalue {
	private Integer star; 
	private String content;
	private String quality; 
	private Integer billDetailId; 
	private Integer accountId;
	private MultipartFile[] images;
}
