package com.gifthub.cart.entity;

import com.gifthub.cart.dto.CartDto;
import com.gifthub.gifticon.entity.Gifticon;
import com.gifthub.global.BaseTimeEntity;
import com.gifthub.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity @Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Cart extends BaseTimeEntity {
    @Id
    @SequenceGenerator(name = "seq_cart", sequenceName = "seq_cart", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cart")
    @Column(name = "cart_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gifticon_id")
    private Gifticon gifticon;

    public CartDto toDto() {
        return CartDto.builder()
                .id(this.id)
                .userDto(this.user.toDto())
                .gifticonDto(this.gifticon.toDto())
                .build();
    }

}
