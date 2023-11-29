package com.gifthub.movement.repository;

import com.gifthub.movement.entity.Movement;
import com.gifthub.movement.entity.QMovement;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gifthub.movement.entity.QMovement.*;

@Repository
@RequiredArgsConstructor
public class MovementRepositoryImpl implements MovementRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Movement> findMovementByFromUserId(Pageable pageable, Long userId) {
        List<Movement> fetch = jpaQueryFactory.select(movement)
                .from(movement)
                .where(movement.fromUser.id.eq(userId))
                .fetch();

        Long count = jpaQueryFactory.select(movement.count())
                .from(movement)
                .where(movement.fromUser.id.eq(userId))
                .fetchOne();

        return new PageImpl<>(fetch, pageable, count);
    }

    @Override
    public Page<Movement> findMovementByToUserId(Pageable pageable, Long userId) {
        List<Movement> fetch = jpaQueryFactory.select(movement)
                .from(movement)
                .where(movement.fromUser.id.eq(userId))
                .fetch();

        Long count = jpaQueryFactory.select(movement.count())
                .from(movement)
                .where(movement.fromUser.id.eq(userId))
                .fetchOne();

        return new PageImpl<>(fetch, pageable, count);
    }

    @Override
    public Page<Movement> findMovementByUserIdContain(Pageable pageable, Long userId) {
        List<Movement> fetch = jpaQueryFactory.select(movement)
                .from(movement)
                .where(movement.fromUser.id.eq(userId).or(movement.toUser.id.eq(userId)))
                .orderBy(movement.createDate.desc())
                .fetch();

        Long count = jpaQueryFactory.select(movement.count())
                .from(movement)
                .where(movement.fromUser.id.eq(userId).or(movement.toUser.id.eq(userId)))
                .fetchOne();

        return new PageImpl<>(fetch, pageable, count);
    }
}
