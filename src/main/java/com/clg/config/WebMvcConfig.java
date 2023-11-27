package com.clg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
         .allowedMethods("*")
         .allowedHeaders("*")
//         .allowedOrigins("http://192.168.1.9:3000")
         .allowedOrigins("*")
         .allowCredentials(false)
         .maxAge(-1);
         }
}
