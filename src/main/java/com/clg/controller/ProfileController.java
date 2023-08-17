package com.clg.controller;

import com.clg.model.Profile;
import com.clg.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    ProfileService profileService;

    @PostMapping("/create")  
    public ResponseEntity<Profile> createProfile(@RequestBody Profile newProfile) {
        Profile createdProfile = profileService.createProfile(newProfile);
        return ResponseEntity.ok(createdProfile);
    }

    @PutMapping("/update/{username}")
    public ResponseEntity<Profile> updateProfile(@PathVariable String username, @RequestBody Profile updatedProfile) {
        Profile existingProfile = profileService.getProfile(username);
        if (existingProfile == null) {
            return ResponseEntity.notFound().build();
        }

        existingProfile.setUserimagePath(updatedProfile.getUserimagePath());
        existingProfile.setDesignation(updatedProfile.getDesignation());
        existingProfile.setAddress(updatedProfile.getAddress());
        existingProfile.setEmail(updatedProfile.getEmail());
        existingProfile.setContactNumber(updatedProfile.getContactNumber());
        existingProfile.setSkills(updatedProfile.getSkills());
        existingProfile.setEducation(updatedProfile.getEducation());
        existingProfile.setProjectHistory(updatedProfile.getProjectHistory());

        Profile updated = profileService.updateProfile(existingProfile);
        return ResponseEntity.ok(updated);
    }

    // Other methods for CRUD operations, delete, etc.
}
