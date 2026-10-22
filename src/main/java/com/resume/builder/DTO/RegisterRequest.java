package com.resume.builder.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    //private String userId;
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username size should be in the length of 3 to 20")
    private String userName;
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String userEmail;
    //private String verificationToken;
    @NotBlank(message = "Password is required")
    @Size(min = 5, max = 20, message = "Password must lie in the range of 5 to 20")
    private String userPassword;
    //private String profileImageUrl;
}
