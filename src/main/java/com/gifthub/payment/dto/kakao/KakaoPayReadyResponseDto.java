package com.gifthub.payment.dto.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 카카오 페이 단건 결제 정보를 담는 DTO
 * api와 쉽게 연동하기 위해 스네이크 케이스로 작성
 */
@Getter
@AllArgsConstructor
@Builder
public class KakaoPayReadyResponseDto {

    private String tid;                         // 결제 고유 번호
    private String next_redirect_app_url;       // 모바일 앱일 경우 redirect url
    private String next_redirect_mobile_url;    // 모바일 웹일 경우 redirect url
    private String next_redirect_pc_url;        // PC 웹일 경우 redirect url
    private String android_app_scheme;          // 카카오페이 결제 화면으로 이동하는 Android 앱 스킴
    private String ios_app_scheme;              // 카카오페이 결제 화면으로 이동하는 iOS 앱 스킴
    private LocalDateTime create_at;            // 결제 준비 요청 시간

}
