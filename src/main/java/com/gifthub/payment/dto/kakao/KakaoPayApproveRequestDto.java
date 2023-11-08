package com.gifthub.payment.dto.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * 카카오 페이 단건 결제 승인 요청 정보를 담는 DTO
 * api와 쉽게 연동하기 위해 스네이크 케이스로 작성
 */
@Getter
@AllArgsConstructor
@Builder
public class KakaoPayApproveRequestDto {

    private String cid;                     // 가맹점 코드
    @Nullable private String cid_secret;    // 가맹점 코드 인증키
    private String tid;                     // 결제 고유번호
    private String partner_order_id;        // 가맹점 주문번호
    private String partner_user_id;         // 가맹점 회원 id
    private String pg_token;                // 결제 승인 인증 토큰
    @Nullable private String payload;       // 저장하고 싶은 값
    @Nullable private Integer total_amount; // 상품 총액

}
