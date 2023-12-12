package com.gifthub.gifticon.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @NotBlank
    private String due;
    @NotBlank
    private String barcode;
    @NotBlank
    private String price;
}
