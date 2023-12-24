package com.gifthub.gifticon.entity;

import com.gifthub.gifticon.dto.BarcodeImageDto;
import com.gifthub.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "barcode_image")
public class BarcodeImage extends BaseTimeEntity {

    @Id
    @SequenceGenerator(name = "seq_barcode_image", sequenceName = "seq_barcode_image", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_barcode_image")
    @Column(name = "barcode_image_id")
    private Long id;

    private String accessUrl;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "gifticon_id")
    private Gifticon gifticon;
    
    public BarcodeImage(Long GifticonId){
        this.id = GifticonId;
        this.accessUrl = "";
    }
    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }


    public BarcodeImageDto toDto(){
        return BarcodeImageDto.builder()
                .id(this.id)
                .accessUrl(this.accessUrl)
                .gifticonDto(gifticon.toDto())
                .build();
    }

}
