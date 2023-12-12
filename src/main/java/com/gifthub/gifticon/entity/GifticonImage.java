package com.gifthub.gifticon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gifthub.gifticon.dto.GifticonImageDto;
import com.gifthub.gifticon.util.GifticonImageUtil;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "gifticon_image")
public class GifticonImage {

    @Id
    @SequenceGenerator(name = "seq_gifticon_image", sequenceName = "seq_gifticon_image", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gifticon_image")
    @Column(name = "gifticon_image_id")
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "gifticonImage", fetch = LAZY)
    private GifticonStorage gifticonStorage;

    private String accessUrl;
    private String originalFileName; // 본래이름
    private String storeFileName; // 서버에 저장될때 사용되는 이름

    public GifticonImage(String originalFileName) {
        this.originalFileName = originalFileName;
        this.storeFileName = getFileName(originalFileName);
        this.accessUrl = "";
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }


    // 이미지 파일의 이름을 저장하기 위한 이름으로 변환하는 메소드
    public String getFileName(String originalFileName) {
        return UUID.randomUUID() + "." + GifticonImageUtil.getFileExtension(originalFileName);
    }

    public GifticonImageDto toGifticonImageDto(){
        return GifticonImageDto.builder()
                .id(this.id)
                .accessUrl(this.accessUrl)
                .originalFileName(this.originalFileName)
                .storeFileName(this.storeFileName)
                .build();
    }

}
