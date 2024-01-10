package com.gifthub.gifticon.dto;

import com.gifthub.gifticon.entity.GifticonImage;
import com.gifthub.gifticon.entity.GifticonStorage;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GifticonImageDto {
    private Long id;
    private GifticonStorage storage;
    private String accessUrl;
    private String originalFileName;
    private String storeFileName;


    public GifticonImage toEntity() {
        return GifticonImage.builder()
                .id(this.id)
                .gifticonStorage(this.storage)
                .accessUrl(this.accessUrl)
                .originalFileName(this.originalFileName)
                .storeFileName(this.storeFileName)
                .build();
    }
    @QueryProjection
    public GifticonImageDto(Long id, GifticonStorage storage, String accessUrl, String originalFileName, String storeFileName) {
        this.id = id;
        this.storage = storage;
        this.accessUrl = accessUrl;
        this.originalFileName = originalFileName;
        this.storeFileName = storeFileName;
    }
}
