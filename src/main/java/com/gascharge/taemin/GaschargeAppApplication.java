package com.gascharge.taemin;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class GaschargeAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(GaschargeAppApplication.class, args);
    }
}
