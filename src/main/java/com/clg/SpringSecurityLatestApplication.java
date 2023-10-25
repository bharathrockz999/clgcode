package com.clg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.HashMap;

@SpringBootApplication
@EnableMongoRepositories
public class SpringSecurityLatestApplication {
	public static HashMap<String,String> codes = new HashMap<>();

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityLatestApplication.class, args);
	}

}
