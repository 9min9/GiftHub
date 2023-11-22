package com.gifthub.event.attendance.repository;

import com.gifthub.event.attendance.entity.Attendance;
import com.gifthub.event.attendance.entity.QAttendance;
import com.gifthub.user.entity.User;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.gifthub.event.attendance.entity.QAttendance.attendance1;

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryImpl implements AttendanceRepositorySupport{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Attendance> findByBetweenDateAndUserId(LocalDateTime startDate, LocalDateTime endDate, Long userId) {
        return queryFactory.selectFrom(attendance1)
                .where(attendance1.modifiedDate.between(startDate, endDate), attendance1.user.id.eq(userId))
                .fetch();
    }

    @Override
    public Long countByCreatedDate(LocalDateTime startDate, LocalDateTime endDate, Long userId) {
        return queryFactory.select(attendance1.count())
                .from(attendance1)
                .where(attendance1.modifiedDate.between(startDate, endDate), attendance1.user.id.eq(userId))
                .fetchOne();
    }
}
