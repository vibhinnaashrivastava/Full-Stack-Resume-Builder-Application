package com.resume.builder.Document;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.lang.annotation.Documented;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_resume")
public class Resume {

    @Id
    @Column(name = "resume_Id")
    private String resumeId;
    @Column(name = "resumeUser_id")
    private String userIdd;
    @Column(name = "resume_title")
    private String resumeTitle;
    @Column(name = "thumbnail_Link")
    private String thumbnailLink;
    @Embedded
    private Template template;
    @Embedded
    private ProfileInfo profileInfo;
    @Embedded
    private ContactInfo contactInfo;
    @ElementCollection
    @CollectionTable(name = "resume_WorkExperience", joinColumns = @JoinColumn(name = "resume_Id"))
    private List<WorkExperience> workExperiences;
    @ElementCollection
    @CollectionTable(name = "resume_educations", joinColumns = @JoinColumn(name = "resume_Id"))
    private List<Education> educations;
    @ElementCollection
    @CollectionTable(name = "resume_skills", joinColumns = @JoinColumn(name = "resume_Id"))
    private List<Skills> skills;
    @ElementCollection
    @CollectionTable(name = "resume_projects", joinColumns = @JoinColumn(name = "resume_Id"))
    private List<Project> projects;
    @ElementCollection
    @CollectionTable(name = "resume_certifications", joinColumns = @JoinColumn(name = "resume_Id"))
    private List<Certification> certifications;
    @ElementCollection
    @CollectionTable(name = "languages", joinColumns = @JoinColumn(name = "resume_Id"))
    private List<Language> languages;
    @ElementCollection
    @CollectionTable(name = "resume_interests", joinColumns = @JoinColumn(name = "resume_Id"))
    @Column(name = "interests")
    private List<String> interests;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Embeddable
    public static class Template{
        private String theme;
        @ElementCollection
        @CollectionTable(name = "colorPoletter", joinColumns = @JoinColumn(name = "resume_Id"))
        @Column(name = "color")
        private List<String> colorPolette;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Embeddable
    public static class ProfileInfo{
        private String profilePreviewUrl;
        private String fullName;
        private String designation;
        private String summary;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Embeddable
    public static class ContactInfo{
        private String email;
        private String phone;
        private String location;
        private String linkedin;
        private String github;
        private String website;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Embeddable
    public static class WorkExperience{
        private String company;
        private String startDate;
        private String endDate;
        private String description;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Embeddable
    public static class Education{
        private String degree;
        private String institution;
        private String startDate;
        private String endDate;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Embeddable
    public static class Skills{
        private String name;
        private Integer progress;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Embeddable
    public static class Project{
        private String title;
        private String description;
        private String github;
        private String liveDemo;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Embeddable
    public static class Certification{
        private String title;
        private String issuer;
        private String year;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Embeddable
    public static class Language{
        private String name;
        private Integer progress;
    }
}
