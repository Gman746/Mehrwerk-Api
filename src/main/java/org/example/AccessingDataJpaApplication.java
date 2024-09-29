package org.example;

import org.example.Model.ApiToken;
import org.example.Repo.ShopRepository;
import org.example.Repo.TokenRepository;
import org.example.Utils.ApiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.Optional;

@SpringBootApplication
public class AccessingDataJpaApplication {

    private static final Logger log = LoggerFactory.getLogger(AccessingDataJpaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AccessingDataJpaApplication.class);
    }

    private static final ApiUtils apiUtils = new ApiUtils();
    private ApiToken apiToken = null;

    @Bean
    public CommandLineRunner demo(TokenRepository tokenRepository, ShopRepository shopRepository) {
        return (args) -> {
            final Optional<ApiToken> tokenFromDb = tokenRepository.findById(1L);
            final Date currentDate = new Date();


           apiToken = (tokenFromDb.isPresent() && currentDate.before(tokenFromDb.get().getExpiryDate())) ? tokenFromDb.get() : apiUtils.generateAuthenticationToken(tokenRepository);
           apiUtils.generateShops(apiToken,shopRepository);
        };
    }

}