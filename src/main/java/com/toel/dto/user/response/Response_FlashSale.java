package com.toel.dto.user.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_FlashSale {


	Integer id;

	LocalDateTime dateStart;

	LocalDateTime dateEnd;

	boolean isDelete;
}
