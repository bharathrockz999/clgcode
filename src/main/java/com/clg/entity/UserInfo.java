package com.clg.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("UserInfo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    @Transient
    public static final String SEQUENCE_NAME = "user_sequence";
    @Id
    private Long id;
    @Indexed(unique = true)
    private String name;
    private String email;
    private String password;
    private String mobile;
    private String roles;
}
