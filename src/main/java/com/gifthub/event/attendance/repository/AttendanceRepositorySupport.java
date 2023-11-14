package com.gifthub.event.attendance.repository;

import com.gifthub.event.attendance.entity.Attendance;
import com.gifthub.user.entity.User;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AttendanceRepositorySupport {


    /**
     * 날짜 기간 동안 특정 유저의 출석 기록을 확인하는 메서드
     */
    List<Attendance> findByBetweenDateAndUserId(@Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate,
                                                @Param("user") Long userId);

}
