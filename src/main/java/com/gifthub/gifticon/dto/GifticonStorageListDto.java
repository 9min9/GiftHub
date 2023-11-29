package com.gifthub.gifticon.dto;

import com.gifthub.gifticon.enumeration.StorageStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class GifticonStorageListDto {
    private Long gifticonStorageId;
    private String barcode;
    private String due;
    private String brand;
    private String productName;
    private String imageUrl;
    private String status;

    private boolean flagInDb;

    @QueryProjection
    @Builder
    public GifticonStorageListDto(Long gifticonStorageId, String brandName, String productName, String barcode, LocalDate due, StorageStatus status, String imageUrl) {
        this.gifticonStorageId = gifticonStorageId;
        this.barcode = (barcode != null) ? barcode : "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.due = (due != null) ? due.format(formatter) : "";
        this.brand = (brandName != null) ? brandName : "";
        this.productName = (productName != null) ? productName : "";
        this.status = status.name();
        this.imageUrl = (imageUrl != null) ? imageUrl : "";
        this.flagInDb = (productName != null) ? true : false;
    }
}
