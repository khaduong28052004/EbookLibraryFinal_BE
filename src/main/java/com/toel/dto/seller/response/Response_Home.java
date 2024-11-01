package com.toel.dto.seller.response;

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
    Integer donChoDuyet;
    double doanhSo;
    double doanhThu;
    Integer luotYeuThich;
    List<Response_DoanhThu> listDoanhThu;
    List<Response_DoanhSo> listDoanhSo;
}
