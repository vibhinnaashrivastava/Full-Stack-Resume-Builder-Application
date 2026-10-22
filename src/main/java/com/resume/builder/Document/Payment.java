package com.resume.builder.Document;

import io.micrometer.core.instrument.binder.netty4.NettyAllocatorMetrics;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Payments")
public class Payment {

    @Id
    @Column(name = "Payment_Id")
    private String paymentId;

    @Column(name = "User_Id")
    private String userId;
    @Column(name = "Razor_OrderId")
    private String razorpayOrderId;
    @Column(name = "Razor_PayId")
    private String razorpayPaymentId;
    @Column(name = "Razor_Signature")
    private String razorpaySignature;

    private String receipt;
    @Column(name = "Amount")
    private Integer amount;
    @Column(name = "Currency")
    private String currency;
    @Column(name = "Plan_Type")
    private String planType;

    @Builder.Default
    private String status = "created"; //Created, Paid or Failed.

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
