package com.resume.builder.Repository;

import com.resume.builder.Document.Resume;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends CrudRepository<Resume, String> {

    List<Resume> findByUserIddOrderByUpdatedAtDesc(String userIdd);

    Optional<Resume> findByUserIddAndResumeId(String userIdd, String resumeId);
}
