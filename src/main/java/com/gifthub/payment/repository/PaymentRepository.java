package com.gifthub.payment.repository;

import com.gifthub.payment.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentRepositorySupport {

    Page<Payment> findByUserIdOrderByIdDesc(Long userId, Pageable pageable);

}
