package com.gifthub.event.attendance.service;

import com.gifthub.event.attendance.dto.AttendanceDto;
import com.gifthub.event.attendance.entity.Attendance;
import com.gifthub.event.attendance.repository.AttendanceRepository;
import com.gifthub.user.entity.User;
import com.gifthub.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    public List<AttendanceDto> getByUserId(Long userId) {
        LocalDateTime now = LocalDateTime.now();

        boolean notDecember = now.getMonthValue() + 1 < 12;

        LocalDateTime start = LocalDateTime.of(now.getYear(), now.getMonthValue(), 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(notDecember ? now.getYear() : now.getYear() + 1, notDecember ? now.getMonthValue() : 1, 1, 0, 0, 0).minusDays(1L);

        return attendanceRepository.findByBetweenDateAndUserId(start, end, userId)
                .stream().map(Attendance::toDto).toList();
    }

    public Long attend(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        Attendance attendance = Attendance.builder()
                .user(user)
                .attendance(1)
                .build();

        Attendance saved = attendanceRepository.save(attendance);

        return saved.getId();
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

    public Long countAttendances(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime today = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0, 0);
        LocalDateTime tomorrow = LocalDateTime.of(now.getYear(), now.getMonth().plus(1), now.getDayOfMonth() - 1, 0, 0, 0);

        return attendanceRepository.countByCreateDate(today, tomorrow, userId);
    }

}
