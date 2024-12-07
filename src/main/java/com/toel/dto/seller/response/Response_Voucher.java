package com.toel.dto.seller.response;

import java.util.Date;
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
    double minOrder;
    double totalPriceOrder;
    double sale;
    int quantity;
    boolean isDelete;
    Date dateStart;
    Date dateEnd;
    Response_Account account;
    Response_TypeVoucher typeVoucher;
}
