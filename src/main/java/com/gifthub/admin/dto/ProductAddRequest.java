package com.gifthub.admin.dto;

import com.gifthub.product.dto.ProductDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductAddRequest {
    @NotBlank
    private String productName;
    @NotBlank
    private String brandName;
    @NotBlank
    private String due;
    @NotBlank
    private String barcode;
    @NotBlank
    private String storageId;
    @NotNull
    private Long price;
    @NotNull
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
