package com.ijdan.training.nomenclatures;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.ijdan.training.nomenclatures")
public class NomenclaturesApplication {
	public static void main(String[] args) {
		SpringApplication.run(NomenclaturesApplication.class, args);
	}
}
