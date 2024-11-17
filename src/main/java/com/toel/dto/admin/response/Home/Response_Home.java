package com.toel.dto.admin.response.Home;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response_Home {
    Integer sumAccount;
    Integer sumShop;
    double doanhThu;
    double loiNhuan;
    List<Response_ChartAccount> listAccount;
    List<Response_ChartDoanhThu> listDoanhThu;
}
