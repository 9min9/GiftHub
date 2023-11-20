package com.gifthub.payment.entity;

import com.gifthub.global.BaseTimeEntity;
import com.gifthub.payment.dto.PaymentDto;
import com.gifthub.payment.enumeration.PayMethod;
import com.gifthub.payment.enumeration.PayStatus;
import com.gifthub.payment.enumeration.Site;
import com.gifthub.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity @Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Payment extends BaseTimeEntity {

    @Id
    @SequenceGenerator(name = "seq_payment", sequenceName = "seq_payment", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_payment")
    @Column(name = "payment_id")
    private Long id;
    private Long price;
    @Enumerated(EnumType.STRING)
    private PayMethod payMethod;
    @Enumerated(EnumType.STRING)
    private Site paymentSite;
    private String payCode;
    @Enumerated(EnumType.STRING)
    private PayStatus payStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void setPaid() {
        this.payStatus = PayStatus.PAID;
    }

    public void setTid(String tid) {
        this.payCode = tid;
    }

    public PaymentDto toDto() {
        return PaymentDto.builder()
                .id(this.id)
                .price(this.price)
                .payMethod(this.payMethod)
                .paymentSite(this.paymentSite)
                .payCode(this.payCode)
                .payStatus(this.payStatus)
                .build();
    }
}
