package com.resume.builder.Controller;

import com.resume.builder.Services.EmailSerivce;
import com.resume.builder.Util.AppConstant;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(AppConstant.EMAIL_RESUME)
public class EmailController {

    private final EmailSerivce emailSerivce;

    @PostMapping(value = AppConstant.RESUME_SEND, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> sendResumeEmail(
            @RequestPart("recipientEmail") String recipientEmail,
            @RequestPart("subject") String subject,
            @RequestPart("message") String message,
            @RequestPart("pdfFile") MultipartFile pdfFile,
            Authentication authentication
            ) throws IOException, MessagingException {
        //STEP 01: Validate the inputs.
        Map<String, Object> newMap = new HashMap<>();
        if(Objects.isNull(recipientEmail) || Objects.isNull(pdfFile)){
            newMap.put("success", false);
            newMap.put("message", "Missing required fields");
            return ResponseEntity.badRequest().body(newMap.toString());
        }
        //STEP 02: Get the file data.
        byte[] pdfBytes = pdfFile.getBytes();
        String originalFilename = pdfFile.getOriginalFilename();
        String filename = Objects.nonNull(originalFilename) ? originalFilename : "resume.pdf";
        //STEP 03: Prepare the email content.
        String emailSubject = Objects.nonNull(subject) ? subject : "Resume Application";
        String emailMessage = Objects.nonNull(message) ? message : "Please find my  resume attached. \n\n Best Resume.";
        //STEP 04: Call the service method.
        emailSerivce.sendEmailWithAttachment(recipientEmail,
                emailSubject,
                emailMessage,
                pdfBytes,
                filename);
        //STEP 05: Return response.
        newMap.put("success", true);
        newMap.put("message", "Resume send successfully " + recipientEmail);
        return ResponseEntity.ok(newMap.toString());
    }
}
