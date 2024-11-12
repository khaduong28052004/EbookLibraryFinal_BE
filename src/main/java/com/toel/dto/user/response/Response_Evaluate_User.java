package com.toel.dto.user.response;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response_Evaluate_User {
	private Integer star;
	private String content;
	private String quality;
	private Integer billDetailId;
	private Integer accountId;
	private MultipartFile[] images;
}
