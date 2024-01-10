package com.gifthub.event.attendance.repository;

import com.gifthub.event.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long>, AttendanceRepositorySupport {
}
