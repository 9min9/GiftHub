package com.gifthub.product.entity;

import com.gifthub.product.dto.ProductDto;
import com.gifthub.gifticon.entity.Gifticon;
import com.gifthub.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Product extends BaseTimeEntity {

    @Id
    @SequenceGenerator(name = "seq_product", sequenceName = "seq_product", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_product")
//    @Column(name = "product_id")
    private Long id;

    @OneToMany(mappedBy = "product")
    private List<Gifticon> gifticons = new ArrayList<>();

    private String name;
    private Long price;
    private String brandName;

    private String category;

    public ProductDto toProductDto() {
        return ProductDto.builder()
                .id(this.id)
                .name(this.name)
                .price(this.price)
                .brandName(this.brandName)
                .category(this.category)
                .build();
    }

}
