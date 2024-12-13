package com.toel.dto.user.resquest;

import org.mapstruct.Mapper;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// @Mapper(componentModel = "spring")
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
