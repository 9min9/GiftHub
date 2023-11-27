package com.gifthub.gifticon.entity;

import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.dto.GifticonStorageListDto;
import com.gifthub.gifticon.dto.ProductDto;
import com.gifthub.gifticon.enumeration.StorageStatus;
import com.gifthub.global.BaseTimeEntity;
import com.gifthub.user.dto.UserDto;
import com.gifthub.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

import static com.gifthub.gifticon.enumeration.StorageStatus.WAIT_REGISTRATION;

// TODO : GifticonStorage를 entity에 넣는게 맞을까?
@Entity
@Getter
@SuperBuilder
//@AllArgsConstructor
@NoArgsConstructor
@Table(name = "gifticon_storage")
@Setter
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gifticon_image_id")
    private GifticonImage gifticonImage;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private StorageStatus storage_status = WAIT_REGISTRATION;


//    public GifticonStorageListDto toDto() {
//        return GifticonStorageListDto.builder()
//                .gifticonStorageId(this.id)
//                .barcode(this.barcode)
//                .due(this.due)
//                .brandName(this.brandName)
//                .productName(this.productName)
//                .imageUrl(this.gifticonImage.getAccessUrl())
//                .build();
//
//    }

//    public GifticonStorageListDto(Long id, String barcode, String due, String brandName, String productName, String )

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
