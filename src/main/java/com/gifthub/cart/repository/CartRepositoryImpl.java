package com.gifthub.cart.repository;

import com.gifthub.cart.entity.Cart;
import com.gifthub.cart.entity.QCart;
import com.gifthub.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gifthub.cart.entity.QCart.cart;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepositorySupport {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Cart> findByUser(Long userId) {
        return queryFactory.selectFrom(cart)
                .where(cart.user.id.eq(userId))
                .fetch();
    }

    @Override
    public Long deleteAllByUserId(Long userId) {
        return queryFactory.delete(cart)
                .where(cart.user.id.eq(userId))
                .execute();
    }


}
