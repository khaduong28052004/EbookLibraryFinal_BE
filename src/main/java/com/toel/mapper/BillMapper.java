package com.toel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.toel.dto.admin.response.ThongKe.Response_TK_Bill;
import com.toel.dto.seller.request.Request_Bill;
import com.toel.dto.seller.response.Response_Bill;
import com.toel.model.Bill;

@Mapper(componentModel = "spring")
public interface BillMapper {
    Response_Bill response_Bill(Bill bill);

    @Mapping(target = "orderStatus", ignore = true)
    Bill bill(Request_Bill request_Bill);

    Response_TK_Bill toResponse_TK_Bill(Bill bill);
}
