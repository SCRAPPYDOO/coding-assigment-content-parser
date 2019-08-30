package com.content;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class ContentParserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContentParserApplication.class, args);
    }
}
