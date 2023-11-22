package com.gifthub.gifticon.entity;

import com.gifthub.gifticon.enumeration.MovementStatus;
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
public class Movement extends BaseTimeEntity {

    @Id
    @SequenceGenerator(name = "seq_movement", sequenceName = "seq_movement", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_movement")
    @Column(name = "movement_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser;

    @ManyToOne
    @JoinColumn(name = "gifticon_id")
    private Gifticon gifticon;

    @Enumerated(EnumType.STRING)
    private MovementStatus movementStatus;

}
