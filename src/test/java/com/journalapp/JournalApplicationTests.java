package com.journalapp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.journalapp.controller.TestAPI;

@SpringBootTest
class JournalApplicationTests {
	@Test
	void testHealthCheck_success() {
		TestAPI test = new TestAPI();
		String response = test.test();
		assertEquals("Hi from Journal Application backend. I am live.", response);
	}
}
