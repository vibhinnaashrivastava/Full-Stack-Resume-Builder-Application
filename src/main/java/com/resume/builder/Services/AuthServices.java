package com.resume.builder.Services;

import com.resume.builder.DTO.LoginRequest;
import com.resume.builder.Document.User;
import com.resume.builder.DTO.AuthResponse;
import com.resume.builder.DTO.RegisterRequest;
import com.resume.builder.Repository.UserRepository;
import com.resume.builder.Util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServices {

    private final UserRepository userRepository;

    private final EmailSerivce emailSerivce;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    @Value("${app.base.url}")
    private String appBaseUrl;

    public AuthResponse register(RegisterRequest request){
        log.info("Inside the Auth Service - Register(): {}", request);

        if (userRepository.existsByUserEmail(request
                .getUserEmail())){
            throw new RuntimeException("User with this email id is already exists !!");
        }

        User newUser = toDocument(request);

        userRepository.save(newUser);

        // TODO: Send Verification E-Mail
        sendVerificationEmail(newUser);

        return toResponse(newUser);

    }


    private void sendVerificationEmail(User newUser) {
        try {
            String baseUrl = appBaseUrl.trim();

            String link = baseUrl
                    + "/auth/resume/verify-email?token="
                    + newUser.getVerificationToken();

            String html = "<div style='font-family:sans-serif'>"
                    + "<h2>Verify your email</h2>"
                    + "<p>Hi " + newUser.getUserName()
                    + ", please confirm your email to activate your account.</p>"
                    + "<p><a href=\"" + link + "\">Verify Email</a></p>"
                    + "<p>Or copy this link: " + link + "</p>"
                    + "<p>This link expires in 24 hours.</p>"
                    + "</div>";

            emailSerivce.sendHtmlEmail(
                    newUser.getUserEmail().trim(),
                    "Verify Your Email",
                    html
            );

        } catch (Exception e) {
            log.error("Failed to send verification email", e);
            throw new RuntimeException(
                    "Failed to send verification email", e
            );
        }
    }


    private AuthResponse toResponse(User newUser){
        return AuthResponse.builder()
                .userId(newUser.getUserId())
                .userName(newUser.getUserName())
                .userEmail(newUser.getUserEmail())
                .profileImageUrl(newUser.getProfileImageUrl())
                .subscriptionPlan(newUser.getSubscriptionPlan())
                .emailVerified(newUser.isEmailVerified())
                .verificationToken(newUser.getVerificationToken())
                .verificationExpired(newUser.getVerficationExpired())
                .build();
    }

    private User toDocument(RegisterRequest request){
         return User.builder()
                 .userId(UUID.randomUUID().toString())
                .userName(request.getUserName())
                .userEmail(request.getUserEmail())
                .userPassword(passwordEncoder.encode(request.getUserPassword()))
                .subscriptionPlan("Basic")
                .emailVerified(false)
                .verificationToken(UUID.randomUUID().toString())
                .verficationExpired(LocalDateTime.now().plusHours(24))
                .build();
    }

    // TODO: Need to understand this part of code !!

    public void verifyEmail(String verificationToken){
        log.info("Inside the AuthSerives - verifyEmail(): {}", verificationToken);
        User userToken = userRepository.findByVerificationToken(verificationToken)
                .orElseThrow(() -> new RuntimeException("Invalid or Expired Verification Token"));
        if(userToken.getVerficationExpired() != null && userToken.getVerficationExpired().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Verification token has expired. Please request for new verification token");
        }
        userToken.setEmailVerified(true);
        userToken.setVerificationToken(null);
        userToken.setVerficationExpired(null);
        User saveUser = userRepository.save(userToken);
    }

    public AuthResponse loginUser(LoginRequest loginRequest){
        User existingUser = userRepository.findByUserEmail(loginRequest.getUserEmail())
                .orElseThrow(() -> new RuntimeException("Invalid Username or Password"));
        if(!passwordEncoder.matches(loginRequest.getUserPassword(), existingUser.getUserPassword())){
            throw new UsernameNotFoundException("Please verify your given email once or contact to the Administrator");
        }
        if(!existingUser.isEmailVerified()){
            throw new RuntimeException("Verify your Email first.");
        }
        String verificationToken = jwtUtil.generateToken(existingUser.getUserId());
        AuthResponse response = toResponse(existingUser);
        response.setVerificationToken(verificationToken);
        return response;
    }

    public void resendVerification(String userEmail){
        //STEP 01: Fetch the user account by email
        User exisitngUser = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User from this email is not present !!"));
        //STEP 02: Check the email is verified
        if(exisitngUser.isEmailVerified()){
            throw new RuntimeException("Email is already verified !! Thanks");
        }
        //STEP 03: Set the new verification token and expires token
        exisitngUser.setVerificationToken(UUID.randomUUID().toString());
        exisitngUser.setVerficationExpired(LocalDateTime.now().plusHours(24));
        //STEP 04: Update the user
        userRepository.save(exisitngUser);
        //STEP 05: Resend the verification email
        sendVerificationEmail(exisitngUser);
    }

    public AuthResponse getProfile(Object principalObject) {
        User existingUser = (User) principalObject;
        return toResponse(existingUser);
    }
}
