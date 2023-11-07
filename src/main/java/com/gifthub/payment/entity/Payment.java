package com.gifthub.payment.entity;

import com.gifthub.global.BaseTimeEntity;
import com.gifthub.payment.enumeration.PayMethod;
import com.gifthub.payment.enumeration.PayStatus;
import com.gifthub.payment.enumeration.Site;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 결제 정보를 저장하는 엔티티
 */
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment extends BaseTimeEntity {

    @Id
    @SequenceGenerator(name = "seq_payment", sequenceName = "seq_payment", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_payment")
    private Long id;
    private Long price;
    @Enumerated(EnumType.STRING)
    private PayMethod payMethod;
    @Enumerated(EnumType.STRING)
    private Site paymentSite;
    private String payCode;
    @Enumerated(EnumType.STRING)
    private PayStatus payStatus;

}
