package com.gifthub.gifticon.entity;

import com.gifthub.gifticon.enumeration.GifticonStorageStatus;
import com.gifthub.user.entity.User;
import jakarta.persistence.*;

@Entity
//todo : class 이름 변경하기
public class GifticonStorage {

    @Id
    @SequenceGenerator(name = "seq_gifticon_storage", sequenceName = "seq_gificon_storage", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gifticon_storage")
    @Column(name = "gifticon_storage_id")
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Gifticon gifticon;

    private GifticonStorageStatus status;

    @ManyToOne
    private GifticonImage gifticonImage;

}
