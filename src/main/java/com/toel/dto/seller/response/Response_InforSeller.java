package com.toel.dto.seller.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

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
    Double averageStarRating; // trung bình sao
    Integer numberOfFollowers; // số luong followers
    Integer numberOfProducts; // số lượng sản phẩm
    Date createAtSeller; // tới gian bán
    long participationTime; // tới gian bán
    Integer trackingNumber; // số lượng theo dõi
    Integer shopCancellationRate; // Tỷ lệ Shop hủy đơn(%)
    Boolean isFollowed;
    // public void setParticipationTime(Date createAtSeller) {
    // this.participationTime = calculateActiveDays(); // Tính ngay khi set
    // }

    // Tính số ngày hoạt động
    public long calculateActiveDays() {
        Date date = new Date();
        if (createAtSeller == null) {
            // throw new IllegalStateException("Participation time is not set.");
            LocalDate createDate = date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            // Tính số ngày giữa ngày tham gia và ngày hiện tại
            return ChronoUnit.DAYS.between(createDate, LocalDate.now());
        }
        // Chuyển Date sang LocalDate
        LocalDate createDate = createAtSeller.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        // Tính số ngày giữa ngày tham gia và ngày hiện tại
        return ChronoUnit.DAYS.between(createDate, LocalDate.now());
    }
}
