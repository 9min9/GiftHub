package com.gifthub.movement.service;

import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.enumeration.MovementStatus;
import com.gifthub.movement.dto.MovementDto;
import com.gifthub.movement.entity.Movement;
import com.gifthub.movement.repository.MovementRepository;
import com.gifthub.user.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class MovementService {

    private final MovementRepository movementRepository;

    public Long move(UserDto from, UserDto to, GifticonDto gifticonDto) {
        Movement movement = Movement.builder()
                .fromUser(from.toEntity())
                .toUser(to.toEntity())
                .gifticon(gifticonDto.toEntityWithKorCategoryName())
                .movementStatus(MovementStatus.SUCCESS)
                .build();

        return movementRepository.save(movement).getId();
    }

    public Page<MovementDto> getByUserIdContain(Pageable pageable, Long userId) {
        return movementRepository.findMovementByUserIdContain(pageable, userId).map(Movement::toMovementDto);
    }

}
