package com.toel.dto.admin.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Bỏ qua các trường null
public class Response_FlashSale {
	Integer id;
	String title;
	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss",
	// timezone = "UTC")
	LocalDateTime dateStart;
	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss",
	// timezone = "UTC")
	LocalDateTime dateEnd;
	boolean isDelete;
	Response_Account account;
	List<Response_FlashSaleDetailNotFlashsale> flashSaleDetails;

}
