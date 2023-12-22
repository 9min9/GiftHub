package com.gifthub.admin.dto;

import com.gifthub.product.dto.ProductDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GifticonAppovalRequest {
    @NotBlank
    private String productName;
    @NotBlank
    private String brandName;
    @NotNull
    private String due;
    @NotBlank
    private String barcode;
    private String storageId;
    @NotNull
    private Long price;
    private Boolean isConfirm;
    private String category;
    private String cancelReason;

    public ProductDto toProductDto() {
        return ProductDto.builder()
                .name(productName)
                .brandName(brandName)
                .price(price)
                .category(category)
                .build();
    }
}
