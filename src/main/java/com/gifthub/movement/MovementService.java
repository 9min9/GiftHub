package com.gifthub.movement;

import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.enumeration.MovementStatus;
import com.gifthub.movement.entity.Movement;
import com.gifthub.user.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class MovementService {

    private final MovementRepository movementRepository;

    public Long move(UserDto from, UserDto to, GifticonDto gifticonDto) {
        Movement movement = Movement.builder()
                .fromUser(from.toEntity())
                .toUser(to.toEntity())
                .gifticon(gifticonDto.toEntity())
                .movementStatus(MovementStatus.SUCCESS)
                .build();

        return movementRepository.save(movement).getId();
    }

}
