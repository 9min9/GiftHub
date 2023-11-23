package com.gifthub.point.controller;

import com.gifthub.point.service.PointService;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO 테스트 필요
@RequestMapping("/api/points")
@RequiredArgsConstructor
@RestController
public class PointController {

    private final PointService pointService;
    private final UserJwtTokenProvider userJwtTokenProvider;

    @PostMapping
    public ResponseEntity<Object> addPoint(@RequestParam("point") Long point,
                                           @RequestHeader HttpHeaders headers
                                           ) {
        try {
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("token").get(0));

            UserDto userDto = pointService.plusPoint(point, userId);

            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("use")
    public ResponseEntity<Object> usePoint(@RequestParam("point") Long point,
                                           @RequestHeader HttpHeaders headers
    ) {
        try {
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("token").get(0));

            UserDto userDto = pointService.usePoint(point, userId);

            if (userDto == null) {
                return ResponseEntity.status(400).body("포인트가 부족합니다. 포인트를 충전 후 다시 시도해주세요.");
            }

            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}
