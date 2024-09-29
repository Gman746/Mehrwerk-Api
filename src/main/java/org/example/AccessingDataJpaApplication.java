package org.example;

import org.example.Repo.TokenRepository;
import org.example.Utils.ApiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AccessingDataJpaApplication {

    private static final Logger log = LoggerFactory.getLogger(AccessingDataJpaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AccessingDataJpaApplication.class);
    }

    private static final ApiUtils apiUtils = new ApiUtils();

    @Bean
    public CommandLineRunner demo(TokenRepository repository) {
        return (args) -> {
            // save a few customers
            apiUtils.generateAuthenticationToken(repository);
        };
    }

}