package com.gifthub.cart.repository;

import com.gifthub.cart.entity.Cart;
import com.gifthub.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("Select c From Cart As c Where user = :user")
    List<Cart> findByUser(@Param("user") User user);

}
