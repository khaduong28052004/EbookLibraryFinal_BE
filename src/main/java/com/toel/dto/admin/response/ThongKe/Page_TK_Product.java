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
public class Page_TK_Product {
    Integer tongSP;
    Integer tongLike;
    Integer tongBill;
    Integer tongEvalue;
    PageImpl<Response_TK_Product> thongke;
}
