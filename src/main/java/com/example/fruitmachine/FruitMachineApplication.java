package com.example.fruitmachine;

import com.example.fruitmachine.domain.ColourSelector;
import com.example.fruitmachine.domain.RandomColourSelector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FruitMachineApplication {

    public static void main(String[] args) {
        SpringApplication.run(FruitMachineApplication.class, args);
    }

    @Bean
    ColourSelector colourSelector() {
        return new RandomColourSelector();
    }
}
