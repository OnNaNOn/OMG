package com.ono.omg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OmgApplication {
	public static void main(String[] args) {
		SpringApplication.run(OmgApplication.class, args);
	}

}
