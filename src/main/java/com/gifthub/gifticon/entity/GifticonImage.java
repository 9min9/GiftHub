package com.gifthub.gifticon.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class GifticonImage {

    @Id
    @SequenceGenerator(name = "seq_gifticon_image", sequenceName = "seq_gifticon_image", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gifticon_image")
    @Column(name = "gifticon_image_id")
    private Long id;

    @OneToOne // Identification RelationShip을 어떻게?
    @JoinColumn(name = "gifticon_id")
    private Gifticon gifticon;

    @OneToOne
    @JoinColumn(name = "gifticon_temp_storage_id")
    private GifticonTempStorage gifticonTempStorage;

    private String imagePath;
    private String imageUrl;
    private String originalFileName;
    private String storeFileName;

}
