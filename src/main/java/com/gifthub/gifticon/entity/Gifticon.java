package com.gifthub.gifticon.entity;

import com.gifthub.user.entity.User;
import com.gifthub.global.BaseTimeEntity;
import com.gifthub.payment.enumeration.SaleStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 기프티콘 정보를 DB에 저장하는 엔티티
 */
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Gifticon extends BaseTimeEntity {

    @Id
    @SequenceGenerator(name = "seq_gifticon", sequenceName = "seq_gificon", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gifticon")
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    private String barcode;
    private LocalDate due;
    private String usablePlace;
    private String productName;
    @Enumerated(EnumType.STRING)
    private SaleStatus saleStatus;
}
