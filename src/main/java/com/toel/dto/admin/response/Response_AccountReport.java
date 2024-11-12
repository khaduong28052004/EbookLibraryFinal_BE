package com.toel.dto.admin.response;

import java.util.Date;

import com.toel.dto.admin.response.ThongKe.Response_TK_Seller;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_AccountReport {
    Integer id;
    String title;
    boolean status;
    Date createAt;
    Date resolve_at;
    String content;
    Response_TK_Seller shop_id;
    int parent_id;
    Response_Account account;
}
