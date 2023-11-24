package com.gifthub.payment.controller;

import com.gifthub.payment.dto.PaymentDto;
import com.gifthub.payment.dto.kakao.*;
import com.gifthub.payment.enumeration.PayMethod;
import com.gifthub.payment.enumeration.PayStatus;
import com.gifthub.payment.enumeration.Site;
import com.gifthub.payment.service.KakaoPayService;
import com.gifthub.payment.service.PaymentService;
import com.gifthub.user.UserJwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/kakao/pay")
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;
    private final PaymentService paymentService;
    private final UserJwtTokenProvider userJwtTokenProvider;

    private final String PARTNER_USER_ID = "Gifthub";

    @PostMapping("/ready")
    public ResponseEntity<Object> ready(
            @RequestBody KakaoPayRequestDto dto,
            BindingResult bindingResult,
            HttpServletRequest request,
            @RequestHeader HttpHeaders headers
    ) {
        KakaoPayReadyResponseDto readyResponseDto = null;

        try {
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

            String cid = "TC0ONETIME";
            String partnerUserId = PARTNER_USER_ID;
            String itemName = dto.getItemName();
            Integer quantity = 1;
            Integer totalAmount = dto.getTotalAmount();

            String paymentMethodType = "MONEY";
            Integer installMonth = 1;

            Integer taxFreeAmount = 0;

            PaymentDto paymentDto = PaymentDto.builder()
                    .price(totalAmount.longValue())
                    .payMethod(PayMethod.MONEY)
                    .paymentSite(Site.KAKAO)
                    .payStatus(PayStatus.PAYING)
                    .build();

            // 결제 정보 저장
            Long paidPaymentId = paymentService.pay(paymentDto);

            String baseUrl = makeBaseUrl(request);

            String approvalUrl = baseUrl + "/api/kakao/pay/approve?paymentId=" + paidPaymentId + "&userId=" + userId;
            String cancelUrl = baseUrl + "/payment/close";
            String failUrl = baseUrl + "/payment/close";

            KakaoPayReadyRequestDto requestDto = KakaoPayReadyRequestDto.builder()
                    .cid(cid)
                    .partner_order_id(paidPaymentId)
                    .partner_user_id(partnerUserId)
                    .item_name(itemName)
                    .quantity(quantity)
                    .total_amount(totalAmount)
                    .tax_free_amount(taxFreeAmount)
                    .approval_url(approvalUrl)
                    .cancel_url(cancelUrl)
                    .fail_url(failUrl)
                    .payment_method_type(paymentMethodType)
                    .install_month(installMonth)
                    .build();

            log.info("[KakaoPay Ready Request]", requestDto);

            readyResponseDto = kakaoPayService.ready(requestDto);

            paymentService.setPayCode(paidPaymentId, readyResponseDto.getTid());

        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("Error");
        } finally {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().build();
            } else {
                return ResponseEntity.ok(readyResponseDto);
            }
        }
    }

    @GetMapping("/approve")
    public ResponseEntity<?> approve(
            @ModelAttribute KakaoApproveRequestDto dto,
            HttpServletRequest request,
            HttpServletResponse response,
            BindingResult bindingResult
        ) {
        String redirectUrl = "";

        HttpHeaders headers = new HttpHeaders();

        try {
            KakaoPayApproveResponseDto approvedResponseDto = null;

            String cid = "TC0ONETIME";
            String tid = paymentService.get(Long.parseLong(dto.getPaymentId())).getPayCode();
            String partnerOrderId = dto.getPaymentId();
            String partnerUserId = PARTNER_USER_ID;

            KakaoPayApproveRequestDto requestDto = KakaoPayApproveRequestDto.builder()
                    .cid(cid)
                    .tid(tid)
                    .partner_order_id(partnerOrderId)
                    .partner_user_id(partnerUserId)
                    .pg_token(dto.getPg_token())
                    .build();

            log.info("[KakaoPay Approve Request]", requestDto);

            paymentService.setPaid(Long.parseLong(dto.getPaymentId()));

            approvedResponseDto = kakaoPayService.approve(requestDto, dto.getUserId());


            if (request.isSecure()) {
                headers.set("location", makeBaseUrl(request) + "/payment/close");
            } else {
                headers.set("location", makeBaseUrl(request) + "/payment/close");
            }
        } finally {
            if (!bindingResult.hasErrors()) {
                return new ResponseEntity<String>("<html><body><script>window.close();</script></body></html>", headers, HttpStatus.TEMPORARY_REDIRECT);
            } else {
                return ResponseEntity.badRequest().build();
            }
        }

    }

    private static String makeBaseUrl(HttpServletRequest request) {
        String baseUrl = "";
        if (request.isSecure()) {
            baseUrl += "https://";
        } else {
            baseUrl += "http://";
        }

        baseUrl += request.getServerName();
        baseUrl += ":";
        baseUrl += request.getLocalPort();
        return baseUrl;
    }

}
