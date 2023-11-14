package com.gifthub.gifticon;

import com.gifthub.gifticon.entity.Gifticon;
import com.gifthub.payment.enumeration.SaleStatus;
import com.gifthub.user.dto.UserDto;
import com.gifthub.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class GifticonDto {

    private Long id;
    private UserDto user;
    private String barcode;
    private LocalDate due;
    private String usablePlace;
    private String productName;
    private SaleStatus saleStatus;

    public Gifticon toEntity() {
        return Gifticon.builder()
                .id(this.id)
                .user(this.user.toEntity())
                .barcode(this.barcode)
                .due(this.due)
                .usablePlace(this.usablePlace)
                .productName(this.productName)
                .saleStatus(this.saleStatus)
                .build();
    }

}
