package com.gifthub.gifticon.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GifticonImage {

    @Id
    @SequenceGenerator(name = "seq_gifticon_image", sequenceName = "seq_gifticon_image", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gifticon_image")
    @Column(name = "gifticon_image_id")
    private Long id;

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

    // 이미지 파일의 확장자를 추출하는 메소드
    public String extractExtension(String originalFileName) {
        int index = originalFileName.lastIndexOf('.');

        return originalFileName.substring(index, originalFileName.length());
    }

    // 이미지 파일의 이름을 저장하기 위한 이름으로 변환하는 메소드
    public String getFileName(String originalFileName) {
        return UUID.randomUUID() + "." + extractExtension(originalFileName);
    }

}
