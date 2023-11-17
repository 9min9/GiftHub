package com.gifthub.event.attendance.service;

import com.gifthub.event.attendance.dto.AttendanceDto;
import com.gifthub.event.attendance.repository.AttendanceRepository;
import com.gifthub.event.attendance.entity.Attendance;
import com.gifthub.point.service.PointService;
import com.gifthub.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceDto getByUserId(Long userId) {
        Optional<Attendance> searched = attendanceRepository.findByUserId(userId);

        if (searched.isEmpty()) {
            return null;
        }

        return searched.orElseThrow().toDto();
    }

    public Integer attend(Long userId) {
        Attendance attendance = null;
        try {
            attendance = attendanceRepository.findByUserId(userId).orElseThrow();
        } catch (Exception e) {
            e.printStackTrace();

            return -1;
        }

        attendance.updateAttendance(attendance.getAttendance() + 1);

        attendanceRepository.save(attendance);

        return attendance.getAttendance();
    }

    public boolean canAttendance(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime today = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        LocalDateTime tomorrow = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth() + 1, 0, 0, 0);

        if (attendanceRepository.findByBetweenDateAndUserId(today, tomorrow, userId).isEmpty()) {
            return true;
        }

        return false;
    }

    public boolean firstAttendance(Long userId) {
        if (attendanceRepository.findByUserId(userId).isEmpty()) {
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

        Attendance saved = attendanceRepository.save(attendance);
    }

    public Integer resetAttendance(Long userId) {
        Attendance attendance = attendanceRepository.findByUserId(userId).orElseThrow();

        return attendance.updateAttendance(0);
    }

}
