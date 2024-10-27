package com.toel.dto.seller.request;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request_Voucher {
    Integer id;
    String name;
    String note;
    double totalPriceOrder;
    double sale;
    int quantity;
    boolean isDelete;
    Date dateStart;
    Date dateEnd;
    Integer typeVoucher;
    Integer account;
}
