package com.epam.esm.restapp.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.epam.esm.entity"})
@EnableJpaRepositories("com.epam.esm.dao")
@EnableJpaAuditing
public class ModuleApplication{

    public static void main(String[] args) {
        SpringApplication.run(ModuleApplication.class, args);
    }
}
