package com.gifthub.event.attendance.controller;

import com.gifthub.event.attendance.dto.AttendanceDto;
import com.gifthub.event.attendance.exception.DuplicateAttendanceException;
import com.gifthub.event.attendance.exception.FailedAttendanceException;
import com.gifthub.event.attendance.service.AttendanceService;
import com.gifthub.global.exception.ExceptionResponse;
import com.gifthub.user.exception.NotLoginedException;
import com.gifthub.point.service.PointService;
import com.gifthub.user.UserJwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    private final ExceptionResponse exceptionResponse;

    private static final Long attendancePoint = 100L;

    @GetMapping
    public ResponseEntity<Object> attendList(@RequestHeader HttpHeaders headers) {
        Long userId = null;
        try {
            userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

            List<AttendanceDto> searched = attendanceService.getByUserId(userId);

            return ResponseEntity.ok().body(searched);
        } catch (NotLoginedException e) {
            return ResponseEntity.badRequest().body(exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage()));
        }

    }

    @PostMapping
    public ResponseEntity<Object> attend(@RequestHeader HttpHeaders headers) {
        Long attendId = null;

        try {
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

            if (userId == null) {
                throw new NotLoginedException();
            }

            if (!attendanceService.canAttendance(userId)) {
                throw new DuplicateAttendanceException();
            }

            attendId = attendanceService.attend(userId);

            pointService.plusPoint(attendancePoint, userId);

            if (attendId == null || attendId == 0) {
                throw new FailedAttendanceException();
            }
        } catch (NotLoginedException | DuplicateAttendanceException | FailedAttendanceException e) {
            return ResponseEntity.badRequest().body(exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage()));
        }

        return ResponseEntity.ok("출석체크 완료! 100포인트를 얻었습니다.");
    }

}
