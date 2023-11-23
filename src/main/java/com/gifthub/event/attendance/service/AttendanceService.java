package com.gifthub.event.attendance.service;

import com.gifthub.event.attendance.dto.AttendanceDto;
import com.gifthub.event.attendance.repository.AttendanceRepository;
import com.gifthub.event.attendance.entity.Attendance;
import com.gifthub.point.service.PointService;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.entity.User;
import com.gifthub.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    public List<AttendanceDto> getByUserId(Long userId) {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime start = LocalDateTime.of(now.getYear(), now.getMonthValue(), 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonthValue() + 1, 1, 0, 0, 0).minusDays(1L);

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

}
