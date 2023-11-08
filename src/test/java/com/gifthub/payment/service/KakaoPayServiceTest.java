package com.gifthub.payment.service;

import com.gifthub.payment.dto.kakao.KakaoPayReadyRequestDto;
import com.gifthub.payment.dto.kakao.KakaoPayReadyResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class KakaoPayServiceTest {

    @Autowired
    KakaoPayService service;

    @Test
    void 서비스가_주입된다() {
        assertThat(service).isNotNull();
    }

    @Test
    void 카카오키값을_가져온다() {
        String kakaoKey = service.getKakaoKey();

        // 카카오키는 length가 32임
        assertThat(kakaoKey.length()).isEqualTo(32);
    }

    @Test
    void 카카오페이_준비요청을_보낸다() {
        KakaoPayReadyResponseDto responseDto = getKakaoPayResponse();

        assertThat(responseDto.getTid().length()).isEqualTo(20);
    }

    private KakaoPayReadyResponseDto getKakaoPayResponse() {
        KakaoPayReadyRequestDto requestDto = KakaoPayReadyRequestDto.builder()
                .cid("TC0ONETIME")
                .partner_order_id(Long.parseLong("1"))
                .partner_user_id("userid")
                .item_name("아이템")
                .quantity(11)
                .total_amount(12000)
                .tax_free_amount(1000)
                .green_deposit(0)
                .payment_method_type("MONEY")
                .install_month(1)
                .approval_url("https://localhost")
                .cancel_url("https://localhost")
                .fail_url("https://localhost")
                .build();

        KakaoPayReadyResponseDto responseDto = service.ready(requestDto);

        return responseDto;
    }

}