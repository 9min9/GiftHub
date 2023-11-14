package com.gifthub.payment.dto.kakao;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class KakaoPayRequestDto {

    private String itemName;
    private Integer totalAmount;

}
