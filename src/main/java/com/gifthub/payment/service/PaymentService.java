package com.gifthub.payment.service;

import com.gifthub.payment.dto.PaymentDto;
import com.gifthub.payment.entity.Payment;
import com.gifthub.payment.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Long pay(PaymentDto dto) {
        return paymentRepository.save(dto.toEntity()).getId();
    }

    public PaymentDto get(Long num) {
        return paymentRepository.findById(num).orElseThrow().toDto();
    }

    public void setPaid(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow();

        payment.setPaid();

        paymentRepository.save(payment);
    }

    public Page<PaymentDto> getAll(Long userId, Pageable pageable) {
        return paymentRepository.findByUserIdOrderByIdDesc(userId, pageable)
                .map(Payment::toDto);
    }

    public void setPayCode(Long id, String tid) {
        Payment payment = paymentRepository.findById(id).orElseThrow();

        payment.setTid(tid);

        paymentRepository.save(payment);
    }

    public Long countMyOrders(Long userId) {
        return paymentRepository.countPaymentByUserId(userId);
    }
}
