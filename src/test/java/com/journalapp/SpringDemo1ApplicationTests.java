package com.journalapp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.journalapp.controller.HealthCheck;

@SpringBootTest
class SpringDemo1ApplicationTests {
	@Test
	void testHealthCheck_success() {
		HealthCheck healthCheck = new HealthCheck();
		String response = healthCheck.healthCheck();
		assertEquals("Hi from Journal Application backend. I am live.", response);
	}
}
