package com.gifthub.cart.dto;

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
public class CartRequestDto {

    private Long id;
    private Long userId;
    private Long gifticonId;

}
