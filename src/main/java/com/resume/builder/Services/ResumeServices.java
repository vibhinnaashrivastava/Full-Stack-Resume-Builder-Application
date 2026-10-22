package com.resume.builder.Services;

import com.resume.builder.DTO.AuthResponse;
import com.resume.builder.DTO.CreateResumeRequest;
import com.resume.builder.Document.Resume;
import com.resume.builder.Repository.ResumeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeServices {

    private final ResumeRepository resumeRepository;
    private final AuthServices authServices;

    public Resume createResume(@Valid CreateResumeRequest request, Object principalObject) {
        //STEP 01: Create the Resume Object.
        Resume newResume = new Resume();
        //STEP 02: Get the current profile.
        AuthResponse authResponse = authServices.getProfile(principalObject);
        //STEP 03: Update the Resume Object.
        newResume.setUserIdd(authResponse.getUserId());
        newResume.setResumeTitle(request.getResumeTitle());
        //STEP 04: Send the default for the resume.
        setDefaultResumeData(newResume);
        //STEP 05: Save the data using ResumeRepository.
        Resume savedResume = resumeRepository.save(newResume);
        //STEP 06: Return the saved Resume Data.
        return savedResume;
    }

    private void setDefaultResumeData(Resume newResume) {
        newResume.setResumeId(UUID.randomUUID().toString());
        newResume.setProfileInfo(new Resume.ProfileInfo());
        newResume.setContactInfo(new Resume.ContactInfo());
        newResume.setWorkExperiences(new ArrayList<>());
        newResume.setEducations(new ArrayList<>());
        newResume.setSkills(new ArrayList<>());
        newResume.setProjects(new ArrayList<>());
        newResume.setCertifications(new ArrayList<>());
        newResume.setLanguages(new ArrayList<>());
        newResume.setInterests(new ArrayList<>());
    }

    public List<Resume> getUserResumes(Object principalObject) {
        //STEP 01: Get the current profile.
        AuthResponse response = authServices.getProfile(principalObject);
        //STEP 02: Call the repository finder method.
        List<Resume> listResumes = resumeRepository.findByUserIddOrderByUpdatedAtDesc(response.getUserId());
        //STEP 03: Return result.
        return listResumes;
    }

    public Resume getResumeById(String resumeId, Object principalObject){
        //STEP 01: Get the current profile.
        AuthResponse response = authServices.getProfile(principalObject);
        //STEP 02: Call the repository finder method and then Return the result.
        return resumeRepository.findByUserIddAndResumeId(response.getUserId(), resumeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resume not found with the id : " + resumeId));
    }

    public Resume updatedResume(String resumeId, Resume updatedData, Object principalObject){
        //STEP 01: Get the current profile.
        AuthResponse response = authServices.getProfile(principalObject);
        //STEP 02: Call the repository finder method.
        Resume currentResume = resumeRepository.findByUserIddAndResumeId(response.getUserId(), resumeId)
                .orElseThrow(() -> new RuntimeException("Resources not found !!"));
        //STEP 03: Update the new data.
        currentResume.setResumeTitle(updatedData.getResumeTitle());
        //currentResume.setResumeId(updatedData.getResumeId());
        currentResume.setThumbnailLink(updatedData.getThumbnailLink());
        currentResume.setTemplate(updatedData.getTemplate());
        currentResume.setProjects(updatedData.getProjects());
        currentResume.setContactInfo(updatedData.getContactInfo());
        currentResume.setWorkExperiences(updatedData.getWorkExperiences());
        currentResume.setSkills(updatedData.getSkills());
        currentResume.setInterests(updatedData.getInterests());
        currentResume.setLanguages(updatedData.getLanguages());
        currentResume.setEducations(updatedData.getEducations());
        currentResume.setCertifications(updatedData.getCertifications());
        //STEP 04: Save the new Data and return.
        return resumeRepository.save(currentResume);
    }

    public void deleteResume(String resumeId, @Nullable Object principal) {
        //STEP 01: Set the current profile.
        AuthResponse response = authServices.getProfile(principal);
        //STEP 02: Call the repository finder method.
        Resume currentResume = resumeRepository.findByUserIddAndResumeId(resumeId, response.getUserId())
                .orElseThrow(() -> new RuntimeException("Resource not found !!"));
        //STEP 03:
        resumeRepository.delete(currentResume);
    }
}
