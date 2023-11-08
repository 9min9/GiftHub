package com.gifthub.payment.dto;

import com.gifthub.payment.entity.Payment;
import com.gifthub.payment.enumeration.PayMethod;
import com.gifthub.payment.enumeration.PayStatus;
import com.gifthub.payment.enumeration.Site;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PaymentDto {

    private Long id;
    private Long price;
    private PayMethod payMethod;
    private Site paymentSite;
    private String payCode;
    private PayStatus payStatus;

    public Payment toEntity() {
        return Payment.builder()
                .id(id)
                .price(price)
                .payMethod(payMethod)
                .paymentSite(paymentSite)
                .payCode(payCode)
                .payStatus(payStatus)
                .build();
    }

    public static PaymentDto toDto(Payment payment) {
        return PaymentDto.builder()
                .id(payment.getId())
                .price(payment.getPrice())
                .payMethod(payment.getPayMethod())
                .paymentSite(payment.getPaymentSite())
                .payCode(payment.getPayCode())
                .payStatus(payment.getPayStatus())
                .build();
    }

}
