package com.resume.builder.Controller;

import com.resume.builder.DTO.CreateResumeRequest;
import com.resume.builder.Document.Resume;
import com.resume.builder.Services.FileUploadService;
import com.resume.builder.Services.ResumeServices;
import com.resume.builder.Util.AppConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(AppConstant.RESUME)
@RequiredArgsConstructor
@Slf4j
public class ResumeController {

    private final ResumeServices resumeServices;
    private final FileUploadService fileUploadService;

    @PostMapping(AppConstant.CREATE_RESUME)
    public ResponseEntity<?> createResume(@Valid @RequestBody CreateResumeRequest request, Authentication authentication){
        //STEP 01: Call the service method.
        Resume newResume = resumeServices.createResume(request, authentication.getPrincipal());
        //STEP 02: Create Response.
        return ResponseEntity.status(HttpStatus.CREATED).body(newResume);
    }

    @GetMapping(AppConstant.GET_RESUME)
    public ResponseEntity<?> getUserResume(Authentication authentication){
        //STEP 01: Call the service method for getUserResume.
        List<Resume> listResume = resumeServices.getUserResumes(authentication.getPrincipal());
        //STEP 02: Return the response.
        return ResponseEntity.ok(listResume);
    }

    @GetMapping(AppConstant.RESUME_ID)
    public ResponseEntity<?> getResumeById(@PathVariable String resumeId, Authentication authentication){
        //STEP 01: Call the service method.
        Resume existingResume = resumeServices.getResumeById(resumeId, authentication.getPrincipal());
        //STEP 02: Return the response.
        return ResponseEntity.ok(existingResume);
    }

    @PutMapping(AppConstant.RESUME_ID)
    public ResponseEntity<?> updateResume(@PathVariable String resumeId, @RequestBody Resume updatedData, Authentication authentication){
        //STEP 01: Call the Service Method.
        Resume updatedResume = resumeServices.updatedResume(resumeId, updatedData, authentication.getPrincipal());
        //STEP 02: Return the response.
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedResume);
    }

    @PutMapping(AppConstant.RESUME_UPLOAD_IMAGES)
    public ResponseEntity<?> uploadResumeImages(@PathVariable String resumeId,
                                                @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
                                                @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
                                                HttpServletRequest httpServletRequest, Authentication authentication) throws IOException {
        //STEP 01: Call the service method.
        Map<String, String> response = fileUploadService.uploadResumeImages(resumeId, thumbnail, profileImage, authentication.getPrincipal());
        //STEP 02: Return the response.
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(AppConstant.RESUME_ID)
    public ResponseEntity<?> deleteResume(@PathVariable String resumeId, Authentication authentication){
        //STEP 01: Call the service method.
        resumeServices.deleteResume(resumeId, authentication.getPrincipal());
        //STEP 02: Return the response.
        return ResponseEntity.status(HttpStatus.GONE).body("Delete Successfully");
    }
}
