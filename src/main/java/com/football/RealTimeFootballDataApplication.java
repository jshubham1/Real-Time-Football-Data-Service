package com.football;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RealTimeFootballDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealTimeFootballDataApplication.class, args);
    }

}
