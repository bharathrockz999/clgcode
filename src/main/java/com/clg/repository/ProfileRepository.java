package com.clg.repository;

import com.clg.model.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProfileRepository extends MongoRepository<Profile, Long> {
    Optional<Profile> findByUsername(String username);
    void deleteByUsername(String username);
}




