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
public class Response_ThonKeSanPham {
    Integer tongLuotBan;
    Integer tongLuotDanhGia;
    Double tongTrungBinhDanhGia;
    Integer tongLuotYeuThich;
    PageImpl<Response_ListSanPham> sanPham;
}
