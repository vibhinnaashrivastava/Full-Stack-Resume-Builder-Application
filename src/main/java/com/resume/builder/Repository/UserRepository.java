package com.resume.builder.Repository;

import com.resume.builder.Document.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {

    Optional<User> findByUserEmail(String userEmail);
    public boolean existsByUserEmail(String userEmail);
    Optional<User> findByVerificationToken(String verificationToken);
}
