package com.gifthub.payment.dto.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 카카오 페이 단건 결제 응답 정보를 담는 DTO
 * api와 쉽게 연동하기 위해 스네이크 케이스로 작성
 */
@Getter
@AllArgsConstructor
@Builder
public class KakaoPayApproveResponseDto {

    private String aid;                 // 요청 고유 번호
    private String tid;                 // 결제 고유 번호
    private String cid;                 // 가맹점 코드
    private String sid;                 // 정기 결제용 id
    private String partner_order_id;    // 가맹점 주문 번호
    private String partner_user_id;     // 가맹점 회원 id
    private String payment_method_type; // 결제 수단 (현금으로 고정)
    private Amount amount;              // 결제 금액 정보
//    private CardInfo card_info;         // 결제 상세 정보 (카드일 경우만 포함)
    private String item_name;           // 상품 이름
    private String item_code;           // 상품 코드
    private Integer quantity;            // 상품 수량
    private LocalDateTime created_at;    // 결제 준비 요청 시각
    private LocalDateTime approved_at;  // 결제 승인 시각
    private String payload;             // 결제 승인 요청에 대해 저장한 값

}
