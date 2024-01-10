package com.gifthub.movement.repository;

import com.gifthub.movement.entity.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long>, MovementRepositorySupport {
}
