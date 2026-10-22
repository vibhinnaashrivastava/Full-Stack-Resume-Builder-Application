package com.resume.builder.Repository;

import com.resume.builder.Document.Payment;
import org.springframework.data.repository.CrudRepository;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends CrudRepository<Payment, SecureRandom> {

    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
    Optional<Payment> findByRazorpayPaymentId(String razorpayPaymentId);
    List<Payment> findByUserIdOrderByCreatedAtDesc(String userId);
    List<Payment> findByStatus(String status);

}
