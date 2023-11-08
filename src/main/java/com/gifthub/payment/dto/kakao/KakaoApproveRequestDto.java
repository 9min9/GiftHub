package com.gifthub.payment.dto.kakao;

import lombok.*;

/**
 * approve 요청을 받는 DTO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KakaoApproveRequestDto {

    private String pg_token;
    private String paymentId;

}
