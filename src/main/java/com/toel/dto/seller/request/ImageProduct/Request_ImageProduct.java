package com.toel.dto.seller.request.ImageProduct;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request_ImageProduct {
    MultipartFile imageFile;
}
