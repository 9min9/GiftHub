package com.gifthub.payment.dto.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * 카카오 페이 단건 결제 결과 정보를 담는 DTO
 * api와 쉽게 연동하기 위해 스네이크 케이스로 작성
 */
@Getter
@AllArgsConstructor
@Builder
public class KakaoPayReadyRequestDto {

    private String cid;                             // 가맹점 번호
    @Nullable private String cid_secret;            // 가맹점 코드 인증키
    private Long partner_order_id;                // 가맹점 주문번호
    private String partner_user_id;                 // 가맹점 회원 id
    private String item_name;                       // 상품명
    @Nullable private String item_code;             // 상품 코드
    private Integer quantity;                       // 상품 수량
    private Integer total_amount;                   // 상품 총 액
    private Integer tax_free_amount;                // 상품 비과세 금액
    @Nullable private Integer vat_amount;           // 상품 부가세 금액
    private Integer green_deposit;                  // 컵 보증금
    private String approval_url;                    // 결제 성공 시 redirect url
    private String cancel_url;                      // 결제 취소시 redirect url
    private String fail_url;                        // 결제 실패시 redirect url
    @Nullable Map<String, String> available_cards;  // 결제 수단으로 사용 가능한 카드
    private String payment_method_type;             // 사용 허가할 결제 수단 CARD or MONEY
    private Integer install_month;                  // 카드 할부 개월
    Map<String, String> custom_json;                // 결제 화면에 보여줄 문구 (카카오와 협의 필요)

}
