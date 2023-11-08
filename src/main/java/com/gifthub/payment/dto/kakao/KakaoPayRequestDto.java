package com.gifthub.payment.dto.kakao;

import lombok.*;

/**
 * html로부터 요청을 받는 DTO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class KakaoPayRequestDto {

    private String itemName;
    private Integer totalAmount;

}
