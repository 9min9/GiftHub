package com.gifthub.event.attendance;

import com.gifthub.user.entity.User;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * 출석체크
 */
@RestController
@RequestMapping("/attendance")
@AllArgsConstructor
public class AttendanceController {

    private final AttendanceService service;

    @PostMapping
    public ResponseEntity<?> attend(HttpSession session) {
        Integer attend = null;

        try {
            Long userId = (Long) session.getAttribute("userId"); // TODO jwt에서 정보 가져와야함
            userId = 1L;

            User user = User.builder() // TODO UserService에서 가져와야함
                    .id(userId)
                    .build();

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다. 로그인을 해주세요");
            }

            if (!service.canAttendance(user)) {
                return ResponseEntity.badRequest().body("출석체크는 하루에 한 번만 가능합니다. 내일 다시 시도해주세요");
            }

            if (service.firstAttendance(user)) {
                service.createAttendance(user);
            }

            attend = service.attend(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (attend == null || attend == 0) {
            return ResponseEntity.badRequest().body("출석체크에 실패했습니다. 다시 시도해주세요");
        }

        return ResponseEntity.ok("출석체크 완료");
    }

}
