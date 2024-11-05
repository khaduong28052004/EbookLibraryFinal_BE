package com.toel.dto.admin.response;

import java.util.Date;

import com.toel.model.Account;
import com.toel.model.TypeVoucher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_Voucher {
    Integer id;
    String name;
    String note;
    double totalPriceOrder;
    double sale;
    int quantity;
    boolean isDelete;
    Date dateStart;
    Date dateEnd;
    TypeVoucher typeVoucher;
    Account account;
}
