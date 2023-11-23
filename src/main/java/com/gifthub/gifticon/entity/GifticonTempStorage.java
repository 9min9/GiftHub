package com.gifthub.gifticon.entity;

import com.gifthub.gifticon.enumeration.GifticonStorageStatus;
import com.gifthub.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
//todo : class 이름 변경하기
public class GifticonTempStorage {

    @Id
    @SequenceGenerator(name = "seq_gifticon_storage", sequenceName = "seq_gificon_storage", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gifticon_storage")
    @Column(name = "gifticon_storage_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "gifticon_id")
    private Gifticon gifticon;


    @OneToOne
    @JoinColumn(name = "gifticon_image_id")
    private GifticonImage gifticonImage;

    private GifticonStorageStatus status;


}
