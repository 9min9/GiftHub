package com.gifthub.gifticon.dto.storage;

import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.dto.GifticonImageDto;
import com.gifthub.gifticon.entity.GifticonStorage;
import com.gifthub.gifticon.enumeration.StorageStatus;
import com.gifthub.product.dto.ProductDto;
import com.gifthub.user.dto.UserDto;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GifticonStorageDto {
    private Long id;
    private String barcode;
    private LocalDate due;
    private String brandName; // 브랜드명(사용처)
    private String productName; // 상품명
    private UserDto user;
    private Long price;
    private GifticonImageDto gifticonImage;
    private StorageStatus storage_status;

    public GifticonStorage toStorageEntity() {
        return GifticonStorage.builder()
                .barcode(barcode)
                .due(due)
                .brandName(brandName)
                .productName(productName)
                .user(user.toEntity())
                .gifticonImage(gifticonImage.toEntity())
                .storage_status(storage_status).build();
    }

    public GifticonDto toGifticonDto(ProductDto productDto){
        return GifticonDto.builder()
                .user(user)
                .barcode(barcode)
                .due(due)
                .brandName(brandName)
                .productName(productName)
                .productDto(productDto)
                .price(productDto.getPrice())
                .build();
    }

}
