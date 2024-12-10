package com.toel.mapper.user;

import org.mapstruct.Mapper;

import com.toel.dto.user.resquest.Request_ProductReport;
import com.toel.model.ProductReport;

@Mapper
public interface ProductReportUserMapper {
	ProductReport productReportToRequesProductReport(Request_ProductReport request);
}
