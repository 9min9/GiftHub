package com.gifthub.gifticon.dto;

import com.gifthub.gifticon.entity.Product;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    private String name;
    private Long price;
    private String brandName;

    private String category;


    public Product toProductEntity() {
        return Product.builder()
                .id(this.id)
                .name(this.name)
                .price(this.price)
                .brandName(this.brandName)
                .category(this.category)
                .build();
    }


    @QueryProjection
    public ProductDto(Long id, String name, Long price, String brandName, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.brandName = brandName;
        this.category = category;
    }
}
