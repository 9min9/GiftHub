package com.gifthub.movement.controller;

import com.gifthub.movement.dto.MovementDto;
import com.gifthub.movement.service.MovementService;
import com.gifthub.user.UserJwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movements")
public class MovementController {

    private final MovementService movementService;
    private final UserJwtTokenProvider userJwtTokenProvider;

    @GetMapping
    public ResponseEntity<Object> getMovementListByUserId(Pageable pageable,
                                                          @RequestHeader HttpHeaders headers) {
        try {
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

            Page<MovementDto> movements = movementService.getByUserIdContain(pageable, userId);

            return ResponseEntity.ok(movements);
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.badRequest().build();
        }
    }

}
