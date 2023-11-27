package com.gifthub.movement.dto;

import com.gifthub.gifticon.entity.Gifticon;
import com.gifthub.gifticon.enumeration.MovementStatus;
import com.gifthub.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MovementDto {

    private Long id;
    private User fromUser;
    private User toUser;
    private Gifticon gifticon;
    private MovementStatus movementStatus;

}
