package com.gifthub.gifticon.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class GifticonStorageListDto {
    private Long id;
    private String brand;
    private String productName;
    private String barcode;
    private String due;
    private String imageUrl;

    @QueryProjection
    public GifticonStorageListDto(Long gifticonStorageId, String brand, String productName, String barcode, LocalDate due, String imageUrl) {
        this.id = gifticonStorageId;
        this.brand = brand;
        this.productName = productName;
        this.barcode = barcode;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.due = due.format(formatter);
        this.imageUrl = imageUrl;
    }
}
