package com.gifthub.admin.dto;

import com.gifthub.gifticon.enumeration.StorageStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class StorageAdminListDto {
    private Long id;
    private String barcode;
    private LocalDate due;
    private String brandName; // 브랜드명(사용처)
    private String productName; // 상품명
    private String userName;
    private String imageUrl;
    private Long price;
    private StorageStatus storageStatus;
    private LocalDate modifiedDate;

    @QueryProjection
    public StorageAdminListDto(Long id, String barcode, LocalDate due, String brandName, String productName, String userName, String imageUrl, StorageStatus storageStatus, Long price, LocalDateTime modifiedDate) {
        this.id = id;
        this.barcode = barcode;
        this.due = due;
        this.brandName = brandName;
        this.productName = productName;
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.storageStatus = storageStatus;
        this.price = price;
        this.modifiedDate = modifiedDate.toLocalDate();
    }
}
