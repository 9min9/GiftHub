package com.gifthub.event.attendance.service;

import com.gifthub.event.attendance.repository.AttendanceRepository;
import com.gifthub.event.attendance.entity.Attendance;
import com.gifthub.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@AllArgsConstructor
public class AttendanceService {

    private final AttendanceRepository repository;

    public Integer attend(Long userId) {
        Attendance attendance = null;
        try {
            attendance = repository.findByUserId(userId).orElseThrow();
        } catch (Exception e) {
            e.printStackTrace();

            return 0;
        }

        attendance.setAttendance(attendance.getAttendance() + 1);

        repository.save(attendance);

        return 1;
    }

    public boolean canAttendance(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime today = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        LocalDateTime tomorrow = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth() + 1, 0, 0, 0);

        if (repository.findByBetweenDateAndUserId(today, tomorrow, userId).isEmpty()) {
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
        User u = User.builder() // TODO jwt에서 가져온 아이디로 UserService에서 유저 정보 가져옴
                .id(userId)
                .build();

        Attendance attendance = Attendance.builder()
                .user(u)
                .attendance(1)
                .build();

        Attendance saved = repository.save(attendance);
    }

}
