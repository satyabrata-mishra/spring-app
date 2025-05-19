package com.journalapp.service;

import org.springframework.http.ResponseEntity;

public interface WeatherService {
	public ResponseEntity<Object> getWeather(String city);
}
