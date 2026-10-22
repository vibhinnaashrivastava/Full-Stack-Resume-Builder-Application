package com.resume.builder.Controller;

import com.resume.builder.DTO.AuthResponse;
import com.resume.builder.DTO.LoginRequest;
import com.resume.builder.DTO.RegisterRequest;
import com.resume.builder.Document.User;
import com.resume.builder.Services.AuthServices;
import com.resume.builder.Services.FileUploadService;
import com.resume.builder.Util.AppConstant;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.micrometer.observation.autoconfigure.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping(AppConstant.AUTH_CONTROLLER)
@RequiredArgsConstructor
public class AuthController {


    private final AuthServices authServices;
    private final FileUploadService fileUploadService;

    @PostMapping(AppConstant.REGISTER)
    public ResponseEntity<?> getRegister(@Valid @RequestBody RegisterRequest request){
        log.info("Inside the AuthController - getRegister(): {}", request);
        try {
            AuthResponse response = authServices.register(request);
            log.info("Response From Services: {}", response);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("Message", exception.getMessage()));
        }
        catch (Exception  exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("Message", "Server Error", "Errors", exception.getMessage()));
        }
    }

    @GetMapping(AppConstant.VERIFY_EMAIL)
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String verificationToken){
        log.info("Inside the AuthController - verifyEmail(): {}", verificationToken);
        authServices.verifyEmail(verificationToken);
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(Map.of("message", "Email verified successfully"));
    }

    @PostMapping(AppConstant.UPLOAD_IMAGES)
    public ResponseEntity<?> uploadImages(@RequestPart("profileImageUrl") MultipartFile file) throws IOException {
        log.info("Inside the AuthController - uploadImages(): {}", file);
        Map<String, String> response = fileUploadService.uploadSingleImage(file);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(AppConstant.LOGIN_USER)
    public ResponseEntity<?> loginRequest(@Valid @RequestBody LoginRequest request){
        AuthResponse response = authServices.loginUser(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PostMapping(AppConstant.RESEND_VERIFICATION)
    public ResponseEntity<?> resendVerification(@RequestBody Map<String, String> body){
        //We have to do :
        //STEP 01: Get the email address
        String userEmail = body.get("userEmail");
        //STEP 02: Add the Validations
        if(Objects.isNull(userEmail)){
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required !!"));
        }
        //STEP 03: Call the service method to resend verification link
        authServices.resendVerification(userEmail);
        //STEP 04: Return Response
        return ResponseEntity.ok(Map.of("success", true, "message", "Verification Email sent successfully"));
    }

    @GetMapping(AppConstant.PROFILE)
    public ResponseEntity<?> getProfile(Authentication authentication){
        //STEP 01: Get the Principal Object
        Object principalObject = authentication.getPrincipal();
        //STEP 02: Call the services method
        AuthResponse response = authServices.getProfile(principalObject);
        //STEP 03: Return the response
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
