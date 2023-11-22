package com.gifthub.gifticon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class GifticonImage {

    @Id
    @GeneratedValue
    @Column(name = "gifticon_image_id")
    private Long id;

    private String imageUrl;
    private String originalFileName;
    private String storeFileName;
    private String imagePath;

}
