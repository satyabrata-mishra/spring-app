package com.journalapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.journalapp.implementation.WeatherImplementation;

@RestController
@RequestMapping("/weather")
public class WeatherController {

	WeatherImplementation weather;

	public WeatherController(WeatherImplementation weather) {
		this.weather = weather;
	}

	@GetMapping("/getWeather/{city}")
	public ResponseEntity<Object> getWeather(@PathVariable String city) {
		return weather.getWeather(city);
	}
}
