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
    private String desc;
    private String crtdBy;
    private Date crtdTme;
    private Boolean approved;
    private String approvedBy;
    private Date apprvdTme;
    private Boolean isDeleted;
    private Integer likes = 0;
    private Integer unlikes = 0;
    private Map<String,String> comments = new HashMap<>();

}
