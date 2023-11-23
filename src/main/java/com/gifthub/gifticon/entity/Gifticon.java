package com.gifthub.gifticon.entity;

import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.enumeration.GifticonStatus;
import com.gifthub.global.BaseTimeEntity;
import com.gifthub.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Gifticon extends BaseTimeEntity {

    @Id
    @SequenceGenerator(name = "seq_gifticon", sequenceName = "seq_gificon", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gifticon")
    @Column(name = "gifticon_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    private String barcode;
    private LocalDate due;
    private String brandName;
    private String productName;
    @Enumerated(EnumType.STRING)
    private GifticonStatus gifticonStatus;
    private Long price;

    public GifticonDto toDto() {
        return GifticonDto.builder()
                .id(this.id)
                .user(this.user.toDto())
                .barcode(this.barcode)
                .due(this.due)
                .brandName(this.brandName)
                .productName(this.productName)
                .gifticonStatus(this.gifticonStatus)
                .price(this.price)
                .build();
    }

    public void addUser(User user) {
        this.user = user;
        user.getGifticons().add(this);
    }

    public void addProduct(Product product) {
        this.product = product;
        product.getGifticons().add(this);
    }

}
