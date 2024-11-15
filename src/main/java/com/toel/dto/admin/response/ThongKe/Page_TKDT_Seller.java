package com.toel.dto.admin.response.ThongKe;

import org.springframework.data.domain.PageImpl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Page_TKDT_Seller {
    Integer tongShop;
    double tongDoanhThu;
    double tongPhi;
    double tongLoiNhuan; 
    PageImpl<Response_TKDT_Seller> thongke;
}
