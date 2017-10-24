package com.ijdan.training.nomenclatures;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class NomenclaturesApplication {
	public static void main(String[] args) {
		SpringApplication.run(NomenclaturesApplication.class, args);
	}
}
