package com.example.backend_course_resgistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.backend_course_resgistration", "modules"})
public class BackendCourseResgistrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendCourseResgistrationApplication.class, args);
    }
}