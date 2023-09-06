package com.clp.kafka.demo.backendforfrontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
public class BackendForFrontendApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendForFrontendApplication.class, args);
    }
}
