package com.gifthub.payment.controller;

import com.gifthub.payment.dto.PaymentDto;
import com.gifthub.payment.dto.kakao.*;
import com.gifthub.payment.enumeration.PayMethod;
import com.gifthub.payment.enumeration.PayStatus;
import com.gifthub.payment.enumeration.Site;
import com.gifthub.payment.service.KakaoPayService;
import com.gifthub.payment.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/kakao/pay")
public class KakaoPayController {

    private final KakaoPayService service;
    private final PaymentService paymentService;

    @PostMapping("/ready")
    public ResponseEntity<?> ready(@RequestBody KakaoPayRequestDto dto, BindingResult bindingResult) {
        KakaoPayReadyResponseDto readyResponseDto = null;

        try {
            String cid = "TC0ONETIME";
            String partnerOrderId = paymentService.nextVal().toString();
            String partnerUserId = "Test123";               // FIXME 세션에서 값 가져오게 변경
            String itemName = dto.getItemName();
            Integer quantity = 1;                           // 고정 (기프티콘 하나씩 거래)
            Integer totalAmount = dto.getTotalAmount();

            String approvalUrl = "https://localhost/api/kakao/pay/approve?paymentId=" + partnerOrderId;
            String cancelUrl = "https://localhost";
            String failUrl = "https://localhost";

            String paymentMethodType = "MONEY";             // 현금 결제만 가능하도록 제한
            Integer installMonth = 1;

            Integer taxFreeAmount = 0;

            KakaoPayReadyRequestDto requestDto = KakaoPayReadyRequestDto.builder()
                    .cid(cid)
                    .partner_order_id(Long.parseLong(partnerOrderId))
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

            readyResponseDto = service.ready(requestDto);

            PaymentDto paymentDto = PaymentDto.builder()
                    .id(Long.parseLong(partnerOrderId))
                    .price(totalAmount.longValue())
                    .payMethod(PayMethod.MONEY)
                    .paymentSite(Site.KAKAO)
                    .payCode(readyResponseDto.getTid())
                    .payStatus(PayStatus.PAYING)
                    .build();

            Long paidPaymentId = paymentService.pay(paymentDto);
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
    public void approve(@ModelAttribute KakaoApproveRequestDto dto, HttpServletRequest request, HttpServletResponse response, BindingResult bindingResult) {
        KakaoPayApproveResponseDto approvedResponseDto = null;

        String cid = "TC0ONETIME";
        String tid = paymentService.get(Long.parseLong(dto.getPaymentId())).getPayCode();
        String partnerOrderId = dto.getPaymentId();
        String partnerUserId = "Test123";

        KakaoPayApproveRequestDto requestDto = KakaoPayApproveRequestDto.builder()
                .cid(cid)
                .tid(tid)
                .partner_order_id(partnerOrderId)
                .partner_user_id(partnerUserId)
                .pg_token(dto.getPg_token())
                .build();

        log.info("[KakaoPay Approve Request]", requestDto);

        paymentService.setPaid(Long.parseLong(dto.getPaymentId()));
        approvedResponseDto = service.approve(requestDto);

        try {
            if (request.isSecure()) {
                response.sendRedirect("https://localhost/payment/close");
            } else {
                response.sendRedirect("http://localhost/payment/close");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
