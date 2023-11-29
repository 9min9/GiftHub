package com.gifthub.movement.repository;

import com.gifthub.movement.entity.Movement;
import com.gifthub.user.dto.UserDto;
import org.apache.tools.ant.taskdefs.Move;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long>, MovementRepositorySupport {
}
