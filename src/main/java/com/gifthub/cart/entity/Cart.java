package com.gifthub.cart.entity;

import com.gifthub.gifticon.entity.Gifticon;
import com.gifthub.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {

    @Id
    @SequenceGenerator(name = "seq_cart", sequenceName = "seq_cart", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cart")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gifticon_id")
    private Gifticon gifticon;

}
