package com.gifthub.gifticon.dto;

import com.gifthub.gifticon.dto.storage.GifticonStorageDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GifticonRegisterRequest {

    @NotNull
    private Long storageId;
    @NotBlank
    private String productName;
    @NotBlank
    private String brandName;
    @NotNull
    private LocalDate due;
    @NotBlank
    private String barcode;
    @NotNull
    private Long price;


    public GifticonStorageDto storageDto() {
        return GifticonStorageDto.builder()
                .id(storageId)
                .productName(productName)
                .brandName(brandName)
                .due(due)
                .barcode(barcode)
                .price(price)
                .build();
    }



}
