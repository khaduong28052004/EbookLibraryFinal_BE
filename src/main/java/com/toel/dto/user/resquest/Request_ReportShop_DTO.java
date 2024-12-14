package com.toel.dto.user.resquest;

import java.util.Date;

import org.mapstruct.Mapper;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
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
    private Boolean status;

    @NotNull(message = "Không để trống createAt")
    private Date createAt;

    @NotBlank(message = "Không để trống content")
    private String content;

    @NotNull(message = "Không để trống title")
    private String title;

    private MultipartFile[] images;
}
