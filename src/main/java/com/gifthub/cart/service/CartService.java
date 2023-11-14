package com.gifthub.cart.service;

import com.gifthub.cart.dto.CartDto;
import com.gifthub.cart.entity.Cart;
import com.gifthub.cart.repository.CartRepository;
import com.gifthub.gifticon.entity.Gifticon;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public Long addToCart(CartDto dto) {
        Cart saved = cartRepository.save(dto.toEntity());

        return saved.getId();
    }

    public void removeFromCart(Long id) {
        cartRepository.deleteById(id);
    }

    public List<CartDto> findByUser(Long userId) {
        return cartRepository.findByUser(userId).stream().map(Cart::toDto).toList();
    }

}
