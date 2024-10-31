package com.toel.mapper.seller;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.toel.dto.seller.request.Request_Bill;
import com.toel.dto.seller.response.Response_Bill;
import com.toel.model.Bill;

@Mapper(componentModel = "spring")
public interface Seller_BillMapper {
    Response_Bill response_Bill(Bill bill);

    @Mapping(target = "orderStatus", ignore = true)
    Bill bill(Request_Bill request_Bill);
}
