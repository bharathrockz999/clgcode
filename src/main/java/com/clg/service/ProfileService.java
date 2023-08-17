package com.clg.service;

import com.clg.model.Profile;
import com.clg.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    @Autowired
    ProfileRepository profileRepository;

    public Profile createProfile(Profile profileData) {
        return profileRepository.save(profileData);
    }

    public Profile getProfile(String username) {
        return profileRepository.findByUsername(username);
    }

    public Profile updateProfile(Profile updatedProfile) {
        return profileRepository.save(updatedProfile);
    }

}


