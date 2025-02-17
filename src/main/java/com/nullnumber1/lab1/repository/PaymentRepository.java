package com.nullnumber1.lab1.repository;

import com.nullnumber1.lab1.model.Payment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
  List<Payment> findAllByUserId(Long userId);

  List<Payment> findAllByStatus(String status);
}
