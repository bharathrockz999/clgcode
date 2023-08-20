package com.clg.repository;

import com.clg.model.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProfileRepository extends MongoRepository<Profile, Long> {
    Profile findByUsername(String username);


}




