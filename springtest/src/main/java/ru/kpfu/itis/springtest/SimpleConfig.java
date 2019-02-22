package ru.kpfu.itis.springtest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SimpleConfig {

    @Bean
    public Converter converterToUSD() {
        return new Converter(70);
    }

    @Bean
    public Converter converterToEuro() {
        return new Converter(80);
    }
}
