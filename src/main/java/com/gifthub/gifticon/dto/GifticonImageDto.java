package com.gifthub.gifticon.dto;

import com.gifthub.gifticon.entity.GifticonImage;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GifticonImageDto {
    private Long id;
    private String accessUrl;
    private String originalFileName;
    private String storeFileName;


    public GifticonImage toEntity() {

        return GifticonImage.builder()
                .id(this.id)
                .accessUrl(this.accessUrl)
                .originalFileName(this.originalFileName)
                .storeFileName(this.storeFileName)
                .build();
    }
}
