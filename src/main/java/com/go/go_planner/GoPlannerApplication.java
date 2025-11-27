package com.go.go_planner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GoPlannerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoPlannerApplication.class, args);
    }
}