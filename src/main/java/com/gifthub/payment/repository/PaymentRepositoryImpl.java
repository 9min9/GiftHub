package com.gifthub.payment.repository;

import com.gifthub.payment.entity.QPayment;
import com.gifthub.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.gifthub.payment.entity.QPayment.*;


@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Long countPaymentByUserId(Long userId) {
        return jpaQueryFactory.select(payment.count())
                .from(payment)
                .where(payment.user.id.eq(userId))
                .fetchOne();
    }

}
