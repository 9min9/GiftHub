package com.gifthub.point.controller;

import com.gifthub.point.service.PointService;
import com.gifthub.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/points")
@RequiredArgsConstructor
@RestController
public class PointController {

    private final PointService pointService;

    @PostMapping
    public ResponseEntity<Object> addPoint(@RequestParam("point") Long point) {
        try {
            Long userId = 1L; // TODO jwt에서 가져옴

            UserDto userDto = pointService.plusPoint(point, userId);

            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("use")
    public ResponseEntity<Object> usePoint(@RequestParam("point") Long point) {
        try {
            Long userId = 1L; // TODO jwt에서 가져옴

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