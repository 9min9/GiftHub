package com.gifthub.gifticon.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Builder
public class GifticonMessageDto {

    private String productName;
    private String brand;
    private String barcode;
    private LocalDate due;


    @QueryProjection
    public GifticonMessageDto(String productName, String brand, String barcode, LocalDate due) {
        this.productName = productName;
        this.brand = brand;
        this.barcode = barcode;
        this.due = due;
    }

}
