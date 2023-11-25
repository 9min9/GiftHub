package com.gifthub.gifticon.entity;

import jakarta.persistence.*;
import lombok.*;

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

    private String imagePath;
    private String originalFileName; // 이걸 없애고, 아래걸로
//    private String storeFileName;

}
