package com.resume.builder.Services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.resume.builder.DTO.AuthResponse;
import com.resume.builder.Document.Resume;
import com.resume.builder.Repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileUploadService {

    private final Cloudinary cloudinary;
    private final AuthServices authServices;
    private final ResumeRepository resumeRepository;

    public Map<String, String> uploadSingleImage(MultipartFile file) throws IOException {
        log.info("Inside the FileUploadService - uploadSingleImage()");
        Map imageUploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resources_type", "image"));
        return Map.of("imageUrl", imageUploadResult.get("secure_url").toString());
    }

    public Map<String, String> uploadResumeImages(String resumeId,
                                                  MultipartFile thumbnail,
                                                  MultipartFile profileImage,
                                                  @Nullable Object principal) throws IOException {
        //STEP 01: Get the current profile.
        AuthResponse response = authServices.getProfile(principal);
        //STEP 02: Get the existing resume.
        Resume presentResume = resumeRepository.findByUserIddAndResumeId(resumeId, response.getUserId())
                .orElseThrow(() -> new RuntimeException("Resource not found !!"));
        //STEP 03: Upload the images and set the resume.
        Map<String, String> returnValue = new HashMap<>();
        Map<String, String> uploadResult;
        if(thumbnail != null && !thumbnail.isEmpty()){
            uploadResult = uploadSingleImage(thumbnail);
            presentResume.setThumbnailLink(uploadResult.get("imageUrl"));
            returnValue.put("ThumbnailLink", uploadResult.get("imageUrl"));
        }
        uploadResult = uploadSingleImage(profileImage);
        if(Objects.isNull(presentResume.getProfileInfo())){
            presentResume.setProfileInfo(new Resume.ProfileInfo());
        }
        presentResume.getProfileInfo().setProfilePreviewUrl(uploadResult.get("imageUrl"));
        returnValue.put("ProfileImagePreview", uploadResult.get("imageUrl"));

        //STEP 04: Update the details into database.
        resumeRepository.save(presentResume);
        returnValue.put("message", "Images Upload Successfully !!");
        //STEP 05: Return the result.
        return returnValue;
    }
}
