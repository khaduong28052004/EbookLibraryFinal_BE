package com.toel.dto.user.resquest;

import java.util.Date;

import org.mapstruct.Mapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Mapper(componentModel = "spring")
public class Request_ReportShop_DTO {
    @NotNull(message = "Vui lòng kiểm tra account id")
    private Integer accountId;

    @NotNull(message = "Vui lòng kiểm tra shop id")
    private Integer shopId;

    @NotNull(message = "Không để trống status")
    private boolean status;

    @NotNull(message = "Không để trống createAt")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date createAt;

    @NotNull(message = "Không để trống content")
    private String content;

    @NotNull(message = "Không để trống title")
    private String title;

    private MultipartFile[] images;
}
