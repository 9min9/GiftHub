package com.gifthub.payment.dto;

import com.gifthub.payment.entity.Payment;
import com.gifthub.payment.enumeration.PayMethod;
import com.gifthub.payment.enumeration.PayStatus;
import com.gifthub.payment.enumeration.Site;
import com.gifthub.user.dto.UserDto;
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
    private UserDto userDto;

    public Payment toEntity() {
        return Payment.builder()
                .id(id)
                .price(price)
                .payMethod(payMethod)
                .paymentSite(paymentSite)
                .payCode(payCode)
                .payStatus(payStatus)
                .user(userDto.toEntity())
                .build();
    }

}
