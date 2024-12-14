package com.toel.dto.admin.response;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response_Log  {
    Integer id;
    String level;
    Date timestamps;
    String tableName;
    String action_type;
    Object doituongOld;
    Object doituongNew;
    Response_Account account;
}
