package com.toel.dto.seller.response;

import org.springframework.data.domain.PageImpl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response_ThongKeKhachHang {
    Integer tongSoLuotMua;
    Integer tongSoSP;
    Integer tongSoLuotDanhGia;
    Double tongSoTien;
    PageImpl<Response_ListKhachHang> khachHang;
}
