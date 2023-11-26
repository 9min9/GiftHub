package com.gifthub.gifticon.dto;

import com.gifthub.gifticon.entity.Gifticon;
import com.gifthub.gifticon.enumeration.GifticonStatus;
import com.gifthub.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class GifticonQueryDto {

    private Long id;
    private UserDto user;
    private String barcode;
    private LocalDate due;
    private String brandName;
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
                .product(this.productDto.toProductEntity())
                .gifticonStatus(this.gifticonStatus)
                .price(this.price)
                .build();
    }

}
