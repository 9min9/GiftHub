package com.gifthub.product.dto;

import com.gifthub.product.entity.Product;
import com.gifthub.product.enumeration.CategoryName;
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
                .category(CategoryName.valueOf(this.category).getKorName())     //이미 Category가 String으로 많이 사용되어 이렇게 처리함
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
