package com.educative.app.hlv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HlvApplication {
	public static void main(String[] args) {
		SpringApplication.run(HlvApplication.class, args);
	}

}
