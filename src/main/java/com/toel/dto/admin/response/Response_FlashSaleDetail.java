package com.toel.dto.admin.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_FlashSaleDetail {
    Integer id;
    LocalDateTime dateStart;
    LocalDateTime dateEnd;
    boolean isDelete;
    Response_Account account;
}
