package com.toel.dto.admin.response.ThongKe;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.relational.core.sql.In;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Page_TK_Bill {
    Integer tongDangChoXuLy;
    Integer tongDangGiao;
    Integer tongHoanThanh;
    Integer tongHuy;
    PageImpl<Response_TK_Bill> thongke;

}
