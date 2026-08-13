package com.example.github_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class GithubServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GithubServiceApplication.class, args);
	}

}
