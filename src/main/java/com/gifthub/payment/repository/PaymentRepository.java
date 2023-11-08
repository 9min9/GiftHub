package com.gifthub.payment.repository;

import com.gifthub.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = "Select seq_payment.Nextval From dual", nativeQuery = true)
    Long nextVal();

}
