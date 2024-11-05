package com.toel.dto.admin.response;

import java.util.Date;

import com.toel.model.Account;
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
    int parent_id;
    Account account;
}
