package com.toel.mapper;

import org.mapstruct.Mapper;

import com.toel.dto.admin.response.Response_ProductReport;
import com.toel.model.Product;
import com.toel.model.ProductReport;

@Mapper(componentModel = "spring")
public interface ProductReportMapper {
    Response_ProductReport toresponse_ProductReport (ProductReport productReport);
}
