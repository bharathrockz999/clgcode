package com.clg.service;

import com.clg.entity.Blog;
import com.clg.model.Profile;
import com.clg.repository.ProfileRepository;
import com.clg.sequence.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    public Profile createProfile(Profile profileData) {
        profileData.setId(sequenceGeneratorService.generateSequence(profileData.SEQUENCE_NAME));
        return profileRepository.save(profileData);
    }

    public Optional<Profile> getProfile(String username) {
         return profileRepository.findByUsername(username);
    }

    public Profile updateProfile(Profile updatedProfile) {
        return profileRepository.save(updatedProfile);
    }

    public void deleteProfile(String username) {
        profileRepository.deleteByUsername(username);
    }

}


