package com.clg.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.util.*;

@Document("blog")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog {
    @Transient
    public static final String SEQUENCE_NAME = "blog_sequence";
    @Id
    private Long id;
    private String sub;
    private String shortIntro;
    private String desc;
    private String crtdBy;
    private Date crtdTme;
    private Boolean approved;
    private String approvedBy;
    private Date apprvdTme;
    private String status;
    private Boolean visibility; // public true // private false
    List<String> categories = new ArrayList<>();
    private Map<String,String> comments = new HashMap<>();
    private List<String> attachments = new ArrayList<>();
    private Date startDate;
    private Date endDate;
    private Set<String> likes =  new HashSet<>();
    private Set<String> unlikes = new HashSet<>();
}
