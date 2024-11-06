package com.toel.mapper;

import org.mapstruct.Mapper;

import com.toel.dto.admin.response.Response_AccountReport;
import com.toel.model.AccountReport;

@Mapper(componentModel = "spring")
public interface AccountReportMapper {
    Response_AccountReport toResponse_AccountReport(AccountReport accountReport);
}
