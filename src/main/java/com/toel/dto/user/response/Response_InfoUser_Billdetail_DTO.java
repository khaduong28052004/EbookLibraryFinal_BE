package com.toel.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_InfoUser_Billdetail_DTO {
    private Integer userId;
    private String userFullname;
    private String userPhone;
    private String userAddress;
}
