package com.toel.dto.seller.response;

import java.util.Date;

import org.hibernate.annotations.Nationalized;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response_InforSeller {

    Integer idSeller;
    String avatar;
    // String phone; // không cần thiết
    String background;
    String shopName;
    String district; // huyện
    Integer averageStarRating; // trung bình sao
    Integer numberOfFollowers; // số luong followers
    Integer numberOfProducts; // số lượng sản phẩm
    Date participationTime; // tới gian bán
    Integer trackingNumber; // số lượng theo dõi
    Integer shopCancellationRate; // Tỷ lệ Shop hủy đơn(%)

}
