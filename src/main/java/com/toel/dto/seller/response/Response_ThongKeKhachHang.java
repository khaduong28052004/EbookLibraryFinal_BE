package com.toel.dto.seller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_ThongKeKhachHang {
    String name;
    Integer soSanPham;
    Integer luotMua;
    Integer luotDanhGia;
    double soTien;
}