package com.toel.dto.user.resquest;

import java.util.Date;

import org.mapstruct.Mapper;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Mapper(componentModel = "spring")
public class Request_Evaluate_User {
	@NotNull(message = "Vui lòng để lại đánh giá")
	private Integer star;

	private String content;

	@NotNull(message = "Cần thông tin đơn hàng")
	private Integer billDetailId;

	@NotNull(message = "Cần thông tin tài khoản")
	private Integer accountId;

	@NotNull(message = "Cần thông tin sản phẩm")
	private Integer productId;

	private MultipartFile[] images;
}
