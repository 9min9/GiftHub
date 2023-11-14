package com.gifthub.payment.dto.kakao;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KakaoApproveRequestDto {

    private String pg_token;
    private String paymentId;

}
