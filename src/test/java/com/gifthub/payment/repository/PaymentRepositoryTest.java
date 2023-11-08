package com.gifthub.payment.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PaymentRepositoryTest {

    @Autowired
    PaymentRepository repository;

    @Test
    @Transactional
    public void 시퀀스값을_반환한다() {
        Long num = repository.nextVal();

        assertThat(num).isNotNull();
    }

}