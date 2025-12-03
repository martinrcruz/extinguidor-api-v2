package com.extinguidor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories
@EnableTransactionManagement
@org.springframework.scheduling.annotation.EnableScheduling
public class ExtinguidorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExtinguidorApplication.class, args);
    }
}

