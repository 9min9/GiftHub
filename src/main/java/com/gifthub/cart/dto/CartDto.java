package com.gifthub.cart.dto;

import com.gifthub.cart.entity.Cart;
import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.user.dto.UserDto;
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
    private UserDto userDto;
    private GifticonDto gifticonDto;

    public Cart toEntity() {
        return Cart.builder()
                .id(this.id)
                .user(this.userDto.toEntity())
                .gifticon(this.gifticonDto.toEntity())
                .build();
    }

}
