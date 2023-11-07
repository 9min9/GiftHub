package com.gifthub.payment.entity;

import com.gifthub.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 결제 테이블과 멤버테이블에 다대다 관계를 만들기 위한 엔티티
 */
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pay {

    @EmbeddedId
    private PayId payId;

}

@Embeddable
class PayId {

    @JoinColumn(name = "payee_id")
    @ManyToOne
    private User payee;

    @JoinColumn(name = "payer_id")
    @ManyToOne
    private User payer;

    @JoinColumn(name = "payment_id")
    @ManyToOne
    private Payment payment;

}