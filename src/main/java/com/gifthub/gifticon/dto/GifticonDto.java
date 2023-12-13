package com.gifthub.gifticon.dto;

import com.gifthub.gifticon.entity.Gifticon;
import com.gifthub.gifticon.entity.GifticonStorage;
import com.gifthub.gifticon.enumeration.GifticonStatus;
import com.gifthub.product.dto.ProductDto;
import com.gifthub.user.dto.UserDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDate;


@NoArgsConstructor
@Builder
@Getter @Setter
@AllArgsConstructor
public class GifticonDto {

    private Long id;
    private UserDto user;
    private String barcode;
    private LocalDate due;
    private String brandName;
    private String productName;
    private ProductDto productDto;
    private GifticonStatus gifticonStatus;
    private Long price;

    public Gifticon toEntity() {
        return Gifticon.builder()
                .id(this.id)
                .user(this.user.toEntity())
                .barcode(this.barcode)
                .due(this.due)
                .brandName(this.brandName)
                .productName(this.productName)
                .product(productDto.toProductEntity())
                .gifticonStatus(this.gifticonStatus)
                .price(this.price)
                .build();
    }

    public Gifticon toEntityWithKorCategoryName() {
        return Gifticon.builder()
                .id(this.id)
                .user(this.user.toEntity())
                .barcode(this.barcode)
                .due(this.due)
                .brandName(this.brandName)
                .productName(this.productName)
                .product(productDto.toProductEntityWithKorCategoryName())
                .gifticonStatus(this.gifticonStatus)
                .price(this.price)
                .build();
    }
    @QueryProjection
    public GifticonDto(Long id, UserDto user, String barcode, LocalDate due, String brandName, String productName, GifticonStatus gifticonStatus, Long price) {
        this.id = id;
        this.user = user;
        this.barcode = barcode;
        this.due = due;
        this.brandName = brandName;
        this.productName = productName;
        this.gifticonStatus = gifticonStatus;
        this.price = price;
    }

    public GifticonStorage toStorageEntity(GifticonImageDto imageDto) {
       return GifticonStorage.builder()
                .user(user.toEntity())
                .barcode(barcode)
                .due(due)
                .brandName(brandName)
                .productName(productName)
                .gifticonImage(imageDto.toEntity())
                .build();
    }

    public Gifticon toEntityExceptProduct() {
        return Gifticon.builder()
                .id(this.id)
                .user(this.user.toEntity())
                .barcode(this.barcode)
                .due(this.due)
                .brandName(this.brandName)
                .productName(this.productName)
                .gifticonStatus(this.gifticonStatus)
                .price(this.price)
                .build();
    }
}
