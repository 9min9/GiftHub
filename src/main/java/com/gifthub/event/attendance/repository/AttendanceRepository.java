package com.gifthub.event.attendance.repository;

import com.gifthub.event.attendance.entity.Attendance;
import com.gifthub.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long>, AttendanceRepositorySupport {
    Optional<Attendance> findByUserId(Long userId);
}
