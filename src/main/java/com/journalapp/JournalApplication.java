package com.journalapp;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.journalapp.utils.Logger1;

@SpringBootApplication
@EnableScheduling
public class JournalApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(JournalApplication.class, args);
		ConfigurableEnvironment enviornment = context.getEnvironment();
		new Logger1().info("Current running enviornment is " + Arrays.toString(enviornment.getActiveProfiles()));
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
