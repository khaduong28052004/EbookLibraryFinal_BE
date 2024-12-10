package com.toel.dto.user;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlatformDTO {
      private String token;
    private String address;
    private String phone;
    private String email;
    private List<CategoryImageDTO> categories;
}


