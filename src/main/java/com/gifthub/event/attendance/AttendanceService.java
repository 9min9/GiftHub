package com.gifthub.event.attendance;

import com.gifthub.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@AllArgsConstructor
public class AttendanceService {

    private final AttendanceRepository repository;

    public Integer attend(User user) {
        return repository.plusOneAttendance(user);
    }

    public boolean canAttendance(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime today = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        LocalDateTime tomorrow = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth() + 1, 0, 0, 0);

        if (repository.findByBetweenDateAndUserId(today, tomorrow, user).isEmpty()) {
            return true;
        }

        return false;
    }

    public boolean firstAttendance(Long userId) {
        if (repository.findByUserId(userId).isEmpty()) {
            return true;
        }

        return false;
    }

    public void createAttendance(Long userId) {
        User u = User.builder() // TODO 세션에서 가져온 아이디로 UserService에서 유저 정보 가져옴
                .id(userId)
                .build();

        Attendance attendance = Attendance.builder()
                .user(u)
                .attendance(1)
                .build();

        Attendance saved = repository.save(attendance);
    }

}
