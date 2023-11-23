package com.gifthub.event.attendance.controller;

import com.gifthub.event.attendance.dto.AttendanceDto;
import com.gifthub.event.attendance.entity.Attendance;
import com.gifthub.event.attendance.service.AttendanceService;
import com.gifthub.point.service.PointService;
import com.gifthub.user.UserJwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendances")
@AllArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final PointService pointService;
    private final UserJwtTokenProvider userJwtTokenProvider;

    private static final Long attendancePoint = 100L;

    @GetMapping
    public ResponseEntity<Object> attendList(@RequestHeader HttpHeaders headers) {
        Long userId = null;
        try {
            userId = userJwtTokenProvider.getUserIdFromToken(headers.get("token").get(0));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("로그인을 해주세요.");
        }

        List<AttendanceDto> searched = attendanceService.getByUserId(userId);

        if (searched != null) {
            return ResponseEntity.ok(searched);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<Object> attend(@RequestHeader HttpHeaders headers) {
        Long attendId = null;

        try {
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("token").get(0));

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다. 로그인을 해주세요");
            }

            if (!attendanceService.canAttendance(userId)) {
                return ResponseEntity.badRequest().body("출석체크는 하루에 한 번만 가능합니다. 내일 다시 시도해주세요");
            }

            attendId = attendanceService.attend(userId);

            if (attendId == 25) {
                pointService.plusPoint(attendancePoint, userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (attendId == null || attendId == 0) {
            return ResponseEntity.badRequest().body("출석체크에 실패했습니다. 다시 시도해주세요");
        }

        return ResponseEntity.ok("출석체크 완료");
    }

}
