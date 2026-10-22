package com.resume.builder.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank(message = "Email must required")
    @Email(message = "Email should be Valid")
    private String userEmail;
    @NotBlank(message = "Password must required")
    private String userPassword;
}
