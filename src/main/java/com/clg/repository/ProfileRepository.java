package com.clg.repository;

import com.clg.model.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProfileRepository extends MongoRepository<Profile, String> {
    Profile findByUsername(String username);

    // Other custom query methods can be defined here
}


//package com.clg.repository;
//import com.clg.entity.UserInfo;
//import com.clg.model.Profile;
//import org.springframework.data.mongodb.repository.MongoRepository;
//
//
//public interface ProfileRepository extends MongoRepository<Profile, Long> 
//{
//   
//}


