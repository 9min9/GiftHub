package com.gifthub.cart.dto;

import com.gifthub.cart.entity.Cart;
import com.gifthub.gifticon.entity.Gifticon;
import com.gifthub.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CartDto {

    private Long id;
    private User user;
    private Gifticon gifticon;

    public Cart toEntity() {
        return Cart.builder()
                .id(this.id)
                .user(this.user)
                .gifticon(this.gifticon)
                .build();
    }

    public static CartDto toDto(Cart cart) {
        return CartDto.builder()
                .id(cart.getId())
                .user(cart.getUser())
                .gifticon(cart.getGifticon())
                .build();
    }

}
