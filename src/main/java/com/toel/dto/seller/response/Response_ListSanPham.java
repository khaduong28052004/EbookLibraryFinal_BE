package com.toel.dto.seller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_ListSanPham {
    String name;
    String theLoai;
    Integer luotBan;
    Integer luotDanhGia;
    Double trungBinhDanhGia;
    Integer luotYeuThich;
}
