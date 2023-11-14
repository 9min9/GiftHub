package com.gifthub.cart.dto;

import com.gifthub.gifticon.entity.Gifticon;
import com.gifthub.user.entity.User;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class CartRequestDto {

    private Long id;
    private Long userId;
    private Long gifticonId;

}
