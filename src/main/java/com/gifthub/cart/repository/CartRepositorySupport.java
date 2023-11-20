package com.gifthub.cart.repository;

import com.gifthub.cart.entity.Cart;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepositorySupport {

    List<Cart> findByUser(@Param("userId") Long userId);

    Long deleteAllByUserId(Long userId);

}
