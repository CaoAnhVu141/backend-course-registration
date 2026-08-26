package com.example.backend_course_resgistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.example.backend_course_resgistration",
        "modules",
        "global"
})
@EnableJpaRepositories(basePackages = "modules")
@EntityScan(basePackages = "modules")
public class BackendCourseResgistrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                BackendCourseResgistrationApplication.class,
                args);
    }
}