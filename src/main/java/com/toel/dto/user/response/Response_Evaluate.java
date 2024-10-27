package com.toel.dto.user.response;

import java.util.Date;

import org.mapstruct.Mapper;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Mapper(componentModel = "spring")
public class Response_Evaluate {
	private Integer star;
	private String content;
	private String quality;
	private Integer billDetailId;
	private Integer accountId;
	private MultipartFile[] images;
}
