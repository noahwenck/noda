package com.wenck.noda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class NodaApplication {

    public static void main(String[] args) {
        SpringApplication.run(NodaApplication.class, args);
    }

}
