package com.gifthub.event.attendance;

import com.gifthub.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Modifying
    @Transactional
    @Query(value = "Update Attendance a Set a.attendance = a.attendance + 1, a.modifiedDate = sysdate Where a.user = :user")
    int plusOneAttendance(@Param("user") User user);

    /**
     * 날짜 기간 동안 특정 유저의 출석 기록을 확인하는 메서드
     * @param startDate
     * @param endDate
     * @param userId
     * @return List<Attendance>
     */
    @Query(value = "Select * From Attendance a Where modified_date Between :startDate And :endDate And user_id = :userId", nativeQuery = true)
    List<Attendance> findByBetweenDateAndUserId(@Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate,
                                                  @Param("userId") Long userId);

    Optional<Attendance> findByUserId(Long userId);
}
