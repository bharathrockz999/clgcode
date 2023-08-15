package com.clg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class SpringSecurityLatestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityLatestApplication.class, args);
	}

}
