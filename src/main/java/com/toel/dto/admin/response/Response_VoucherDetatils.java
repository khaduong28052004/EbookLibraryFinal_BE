package com.toel.dto.admin.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_VoucherDetatils {
    Integer id;
    Response_Account account;
    Response_Bill bill;
    Response_Voucher voucher;
}
