package com.gifthub.gifticon.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class GifticonStorageListDto {
    private Long id;
    private String barcode;
    private String due;
//    private LocalDate due;
    private String brand;
    private String productName;
    private String imageUrl;

    @QueryProjection
    @Builder
    public GifticonStorageListDto(Long gifticonStorageId, String brandName, String productName, String barcode, LocalDate due, String imageUrl) {
        this.id = gifticonStorageId;
        this.barcode = barcode;
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        this.due = due.format(formatter);
//        this.due = due;
        this.brand = brandName;
        this.productName = productName;
        this.imageUrl = imageUrl;
    }
}
