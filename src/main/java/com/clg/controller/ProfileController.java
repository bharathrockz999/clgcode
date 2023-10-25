package com.clg.controller;

import com.clg.model.Profile;
import com.clg.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    ProfileService profileService;

    @PostMapping("/create")
    public ResponseEntity<Profile> createProfile(@RequestBody Profile newProfile) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        newProfile.setUsername(username);
        Profile createdProfile = profileService.createProfile(newProfile);
        return ResponseEntity.ok(createdProfile);
    }

    @PutMapping("/update/{username}")
    public ResponseEntity<Profile> updateProfile(@PathVariable String username, @RequestBody Profile updatedProfile) {
        Optional<Profile> optionalProfile = profileService.getProfile(username);
        if (!optionalProfile.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Profile existingProfile = optionalProfile.get();
        existingProfile.setFirstName(updatedProfile.getFirstName());
        existingProfile.setLastName(updatedProfile.getLastName());
        existingProfile.setUserImagePath(updatedProfile.getUserImagePath());
        existingProfile.setDesignation(updatedProfile.getDesignation());
        existingProfile.setAddress(updatedProfile.getAddress());
        existingProfile.setEmail(updatedProfile.getEmail());
        existingProfile.setContactNumber(updatedProfile.getContactNumber());
        existingProfile.setSkills(updatedProfile.getSkills());
        existingProfile.setEducation(updatedProfile.getEducation());
        existingProfile.setLanguages(updatedProfile.getLanguages());
        Profile updated = profileService.updateProfile(existingProfile);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<String> deleteProfile(@PathVariable String username) {
        profileService.deleteProfile(username);
        return ResponseEntity.ok("deleted");
    }

    @GetMapping("/get/{userName}")
    public ResponseEntity<Object> getProfile(@PathVariable String userName) {
        Optional<Profile> profile = profileService.getProfile(userName);
        return profile.isPresent() ?   ResponseEntity.ok(profile.get()) :  ResponseEntity.ok("Not Found");

    }


}