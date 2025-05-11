package com.SpringDemo1.SpringDemo1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health-check")
public class HealthCheck {

	@GetMapping
	public String healthCheck() {
		return "Hi from Journal Application backend. I am live.";
	}
}
