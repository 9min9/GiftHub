package com.gifthub.payment.controller;

import com.gifthub.payment.pagination.Criteria;
import com.gifthub.payment.pagination.Pagination;
import com.gifthub.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentPage {

    private final PaymentService paymentService;

    @RequestMapping("/close")
    public String close() {
        return "/payment/close";
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "/payment/checkout";
    }

    @GetMapping("orders")
    public String orders(Criteria criteria, Model model) {
        Long userId = 1L; //TODO jwt에서 가져옴

        if (criteria.getSize() == 0L) {
            criteria.setSize(10L);
        }

        if (criteria.getPage() == 0L) {
            criteria.setPage(1L);
        }

        Long myOrderCount = paymentService.countMyOrders(userId);
        criteria.setTotalAmount(myOrderCount);

        Pagination pagination = new Pagination(criteria);

        model.addAttribute("pagination", pagination);

        return "/payment/orders";
    }


}
