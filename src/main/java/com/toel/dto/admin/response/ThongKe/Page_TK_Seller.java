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
public class Page_TK_Seller {
    Integer tongShop;
    PageImpl<Response_TK_Seller> thongke;
}
