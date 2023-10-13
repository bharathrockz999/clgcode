package com.clg.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("profile")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profile {
    @Transient
    public static final String SEQUENCE_NAME = "profile_sequence";
    @Id
    private Long id;
    @Indexed(unique = true)
    private String username;
    private String firstName;
    private String lastName;
    private String userImagePath;
    private String designation;
    private String address;
    private String email;
    private String contactNumber;
    private Map<String, Integer> skills;
    private Map<String, Integer> languages;
    private List<Education> education;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Education {
        private String universityName;
        private String degreeType;
        private Date startDate;
        private Date endDate;
    }

}
