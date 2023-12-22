package com.gifthub.payment.controller;

import com.gifthub.global.error.ErrorResponse;
import com.gifthub.global.exception.ExceptionResponse;
import com.gifthub.payment.dto.PaymentDto;
import com.gifthub.payment.dto.kakao.*;
import com.gifthub.payment.enumeration.PayMethod;
import com.gifthub.payment.enumeration.PayStatus;
import com.gifthub.payment.enumeration.Site;
import com.gifthub.payment.exception.EmptyItemNameException;
import com.gifthub.payment.exception.EmptyPgTokenException;
import com.gifthub.payment.exception.EmptyTotalAmountException;
import com.gifthub.payment.exception.PaidIdMismatchException;
import com.gifthub.payment.service.KakaoPayService;
import com.gifthub.payment.service.PaymentService;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.dto.UserDto;
import com.gifthub.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kakao/pay")
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;
    private final PaymentService paymentService;
    private final UserJwtTokenProvider userJwtTokenProvider;
    private final ErrorResponse errorResponse;
    private final UserService userService;

    private final String PARTNER_USER_ID = "Gifthub";

    @PostMapping("/ready")
    public ResponseEntity<Object> ready(@Valid @RequestBody KakaoPayRequestDto dto,
                                        BindingResult bindingResult,
                                        HttpServletRequest request,
                                        @RequestHeader HttpHeaders headers) {
        KakaoPayReadyResponseDto readyResponseDto = null;

        try {
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

            String cid = "TC0ONETIME";
            String partnerUserId = PARTNER_USER_ID;
            String itemName = dto.getItemName();

            if (isNull(itemName)) {
                throw new EmptyItemNameException();
            }

            Integer quantity = 1;
            Integer totalAmount = dto.getTotalAmount();

            if (isNull(totalAmount) || totalAmount == 0) {
                throw new EmptyTotalAmountException();
            }

            String paymentMethodType = "MONEY";
            Integer installMonth = 1;

            Integer taxFreeAmount = 0;

            UserDto user = userService.getUserById(userId);

            PaymentDto paymentDto = PaymentDto.builder()
                    .price(totalAmount.longValue())
                    .payMethod(PayMethod.MONEY)
                    .paymentSite(Site.KAKAO)
                    .payStatus(PayStatus.PAYING)
                    .userDto(user)
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

        } finally {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(errorResponse.getErrors(bindingResult));
            } else {
                return ResponseEntity.ok(readyResponseDto);
            }
        }
    }

    @GetMapping("/approve")
    public ResponseEntity<?> approve(@Valid @ModelAttribute KakaoApproveRequestDto dto,
                                    BindingResult bindingResult,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {

        String redirectUrl = "";

        HttpHeaders headers = new HttpHeaders();

        try {
            KakaoPayApproveResponseDto approvedResponseDto = null;

            String cid = "TC0ONETIME";
            String tid = paymentService.get(Long.parseLong(dto.getPaymentId())).getPayCode();
            String partnerOrderId = dto.getPaymentId();
            String partnerUserId = PARTNER_USER_ID;

            if (isNull(dto.getPg_token())) {
                throw new EmptyPgTokenException();
            }

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
                headers.set("location", makeBaseUrl(request) + "/payment/close?redirect=true");
            } else {
                headers.set("location", makeBaseUrl(request) + "/payment/close?redirect=true");
            }
        } catch (EmptyItemNameException e) {
            bindingResult.rejectValue(e.getField(), e.getCode(), e.getMessage());
        } finally {
            if (!bindingResult.hasErrors()) {
                return new ResponseEntity<String>("<html><body><script>opener.location.href='/'; window.close();</script></body></html>", headers, HttpStatus.OK);
            } else {
                System.out.println("KakaoPayController.approve");
                for (ObjectError allError : bindingResult.getAllErrors()) {
                    System.out.println(allError.getCode());
                }
                return ResponseEntity.badRequest().body(errorResponse.getErrors(bindingResult));
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

    private static <T> boolean isNull(T t) {
        if (t == null) {
            return true;
        } else {
            return false;
        }
    }

}