package com.gifthub.gifticon.dto;

import com.gifthub.gifticon.entity.GifticonImage;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GifticonImageDto {
    private Long id;
    private String imagePath;
    private String originalFileName;
//    private String storeFileName;


    public GifticonImage toEntity() {

        return GifticonImage.builder()
                .id(this.id)
                .imagePath(this.imagePath)
                .originalFileName(this.originalFileName)
                .build();
    }
}
