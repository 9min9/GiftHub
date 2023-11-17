package com.gifthub.gifticon.dto;

import com.gifthub.gifticon.entity.Gifticon;
import com.gifthub.gifticon.entity.Product;
import com.querydsl.core.annotations.QueryProjection;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class ProductDto {
private Long id;
private String name;
private Long price;
private String brandName;


public Product toProductEntity(){
    return Product.builder()
            .id(this.id)
            .name(this.name)
            .price(this.price)
            .brandName(this.brandName)
            .build();
}
public ProductDto() {
}

@QueryProjection
public ProductDto(Long id, String name, Long price, String brandName) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.brandName = brandName;
}
}
