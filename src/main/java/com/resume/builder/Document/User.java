package com.resume.builder.Document;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "User_Id")
    private String userId;
    @Column(name = "User_Name")
    private String userName;
    @Column(name = "User_Email")
    private String userEmail;
    @Column(name = "User_Password")
    private String userPassword;
    @Column(name = "User_Image")
    private String profileImageUrl;
    @Column(name = "Subscription_Detail")
    private String subscriptionPlan = "Basic";
    @Column(name = "Email_Verification")
    private boolean emailVerified = false;
    @Column(name = "Verification_Token")
    private String verificationToken;
    @Column(name = "Verification_Expiry")
    private LocalDateTime verficationExpired;

    @CreationTimestamp
    @Column(name = "Created_Date", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "Updated_Date")
    private LocalDateTime updatedAt;
}
