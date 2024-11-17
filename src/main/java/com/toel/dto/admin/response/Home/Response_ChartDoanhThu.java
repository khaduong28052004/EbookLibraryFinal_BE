package com.toel.dto.admin.response.Home;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_ChartDoanhThu {
    Double series;
    String labels;
}
