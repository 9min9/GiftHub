package com.gifthub.gifticon.dto;

import com.gifthub.gifticon.entity.BarcodeImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BarcodeImageDto {
    private Long id;
    private String accessUrl;
    private GifticonDto gifticonDto;

    public BarcodeImage toEntity(){
        return BarcodeImage.builder()
                .id(this.id)
                .accessUrl(this.accessUrl)
                .gifticon(this.gifticonDto.toEntity())
                .build();
    }

}
