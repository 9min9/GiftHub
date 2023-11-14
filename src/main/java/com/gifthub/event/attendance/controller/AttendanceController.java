package com.gifthub.event.attendance.controller;

import com.gifthub.event.attendance.service.AttendanceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attendances")
@AllArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<Object> attend() {
        Integer attend = null;

        try {
            Long userId = 1L;

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다. 로그인을 해주세요");
            }

            if (!attendanceService.canAttendance(userId)) {
                return ResponseEntity.badRequest().body("출석체크는 하루에 한 번만 가능합니다. 내일 다시 시도해주세요");
            }

            if (attendanceService.firstAttendance(userId)) {
                attendanceService.createAttendance(userId);
            }

            attend = attendanceService.attend(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (attend == null || attend == 0) {
            return ResponseEntity.badRequest().body("출석체크에 실패했습니다. 다시 시도해주세요");
        }

        return ResponseEntity.ok("출석체크 완료");
    }

}
