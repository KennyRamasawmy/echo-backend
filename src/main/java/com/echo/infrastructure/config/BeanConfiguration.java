package com.echo.infrastructure.config;


import com.echo.domain.port.in.CardService;
import com.echo.domain.port.out.CardRepository;
import com.echo.domain.service.CardServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public CardService cardService(CardRepository cardRepository) {
        return new CardServiceImpl(cardRepository);
    }
}
