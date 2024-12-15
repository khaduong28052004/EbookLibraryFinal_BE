package com.toel.dto.seller.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response_ChatBotCategory {
    String emotion;
    List<com.toel.dto.user.response.Response_Product> products;
}
