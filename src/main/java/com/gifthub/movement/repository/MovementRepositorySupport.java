package com.gifthub.movement.repository;

import com.gifthub.movement.entity.Movement;
import org.apache.tools.ant.taskdefs.Move;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovementRepositorySupport {

    Page<Movement> findMovementByFromUserId(Pageable pageable, Long userId);

    Page<Movement> findMovementByToUserId(Pageable pageable, Long userId);

    Page<Movement> findMovementByUserIdContain(Pageable pageable, Long userId);

}
