package com.gifthub.admin.dto;

import com.gifthub.product.dto.ProductDto;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductAddRequest {
    private String productName;
    private String brandName;
    private String due;
    private String barcode;
    private String storageId;
    private Long price;
    private String category;
    private Boolean isConfirm;
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
