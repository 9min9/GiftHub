package com.gifthub.gifticon.entity;

import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.user.entity.User;
import com.gifthub.global.BaseTimeEntity;
import com.gifthub.payment.enumeration.SaleStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private Long price;

    public GifticonDto toDto() {
        return GifticonDto.builder()
                .id(this.id)
                .user(this.user.toDto())
                .barcode(this.barcode)
                .due(this.due)
                .usablePlace(this.usablePlace)
                .productName(this.productName)
                .saleStatus(this.saleStatus)
                .build();
    }
}
