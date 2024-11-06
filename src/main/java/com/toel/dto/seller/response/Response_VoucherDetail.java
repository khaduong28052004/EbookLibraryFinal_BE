package com.toel.dto.seller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_VoucherDetail {
    Integer id;
    Response_Account account;
    Response_Voucher voucher;
}