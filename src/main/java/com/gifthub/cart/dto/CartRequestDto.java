package com.gifthub.cart.dto;

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
