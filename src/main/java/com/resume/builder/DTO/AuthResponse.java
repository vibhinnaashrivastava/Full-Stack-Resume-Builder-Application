package com.resume.builder.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {

    private String userId;
    private String userName;
    private String userEmail;
    private String profileImageUrl;
    private String subscriptionPlan = "Basic";
    private boolean emailVerified = false;
    private String verificationToken;
    private LocalDateTime verificationExpired;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
