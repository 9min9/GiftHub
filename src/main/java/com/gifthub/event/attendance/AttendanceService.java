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
        Attendance attendance = null;
        try {
            attendance = repository.findByUserId(user.getId()).orElseThrow();
        } catch (Exception e) {
            e.printStackTrace();

            return 0;
        }

        attendance.setAttendance(attendance.getAttendance() + 1);

        repository.save(attendance);

        return 1;
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

    public boolean firstAttendance(User user) {
        if (repository.findByUserId(user.getId()).isEmpty()) {
            return true;
        }

        return false;
    }

    public void createAttendance(User user) {
        User u = User.builder() // TODO jwt에서 가져온 아이디로 UserService에서 유저 정보 가져옴
                .id(user.getId())
                .build();

        Attendance attendance = Attendance.builder()
                .user(u)
                .attendance(1)
                .build();

        Attendance saved = repository.save(attendance);
    }

}
