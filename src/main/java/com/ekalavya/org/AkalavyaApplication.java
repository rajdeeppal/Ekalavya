package com.ekalavya.org;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AkalavyaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AkalavyaApplication.class, args);
	}

}
