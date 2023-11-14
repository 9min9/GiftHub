package com.gifthub.gifticon.entity;

import com.gifthub.gifticon.enumeration.MovementStatus;
import com.gifthub.user.entity.User;
import com.gifthub.global.BaseTimeEntity;
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
public class Movement extends BaseTimeEntity {

    @Id
    @SequenceGenerator(name = "seq_movement", sequenceName = "seq_movement", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_movement")
    private Long id;

    @JoinColumn(name = "from_user_id")
    @ManyToOne
    private User fromUser;

    @JoinColumn(name = "to_user_id")
    @ManyToOne
    private User toUser;

    @JoinColumn(name = "gifticon_id")
    @ManyToOne
    private Gifticon gifticon;

    @Enumerated(EnumType.STRING)
    private MovementStatus movementStatus;

}
