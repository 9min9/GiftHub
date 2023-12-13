package com.gifthub.gifticon.entity;

import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.dto.storage.GifticonStorageDto;
import com.gifthub.gifticon.enumeration.StorageStatus;
import com.gifthub.global.BaseTimeEntity;
import com.gifthub.product.dto.ProductDto;
import com.gifthub.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

import static com.gifthub.gifticon.enumeration.StorageStatus.WAIT_REGISTRATION;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "gifticon_storage")
public class GifticonStorage extends BaseTimeEntity {

    @Id
    @SequenceGenerator(name = "seq_gifticon_storage", sequenceName = "seq_gifticon_storage", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gifticon_storage")
    @Column(name = "gifticon_storage_id")
    private Long id;

    private String barcode;
    private LocalDate due;
    private String brandName; // 브랜드명(사용처)
    private String productName; // 상품명
    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "gifticon_image_id")
    private GifticonImage gifticonImage;


    @Enumerated(EnumType.STRING)
    private StorageStatus storage_status = WAIT_REGISTRATION;

    public void changeBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void changeProductName(String productName) {
        this.productName = productName;
    }

    public void changeBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void changeDue(LocalDate due) {
        this.due = due;
    }

    public void changeStatus(StorageStatus status) {
        storage_status = status;
    }

    public void changePrice(Long price) {
        this.price = price;
    }


    public GifticonStorageDto toStorageDto() {
        return GifticonStorageDto.builder()
                .id(id)
                .barcode(barcode)
                .due(due)
                .brandName(brandName)
                .productName(productName)
                .user(user.toDto())
                .gifticonImage(gifticonImage.toGifticonImageDto())
                .storage_status(storage_status).build();
    }

    public GifticonDto toGifticonDto(ProductDto productDto){
        return GifticonDto.builder()
                .user(this.user.toDto())
                .barcode(this.barcode)
                .due(this.due)
                .brandName(this.brandName)
                .productName(this.productName)
                .price(productDto.getPrice())
                .build();
    }


}
