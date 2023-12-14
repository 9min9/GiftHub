package com.gifthub.admin.dto;

import com.gifthub.product.dto.ProductDto;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GifticonAppovalRequest {
    private String productName;
    private String brandName;
    private String due;
    private String barcode;
    private String storageId;
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
