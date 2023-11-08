package com.gifthub.payment.dto.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Amount {

    private Integer total;          // 전체 결제 금액
    private Integer tax_free;       // 비과세 금액
    private Integer vat;            // 부가세 금액
    private Integer point;          // 사용한 포인트 금액
    private Integer discount;       // 할인 금액
    private Integer green_deposit;  // 컵 보증금

}
