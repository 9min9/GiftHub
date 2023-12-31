package com.gifthub.movement.entity;

import com.gifthub.gifticon.entity.Gifticon;
import com.gifthub.gifticon.enumeration.MovementStatus;
import com.gifthub.global.BaseTimeEntity;
import com.gifthub.movement.dto.MovementDto;
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

    public MovementDto toMovementDto() {
        return MovementDto.builder()
                .id(this.id)
                .fromUser(this.fromUser.toDto())
                .toUser(this.toUser.toDto())
                .gifticon(this.gifticon.toDtoWithProduct())
                .movementStatus(this.movementStatus)
                .build();
    }

}
