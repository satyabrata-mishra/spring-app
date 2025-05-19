package com.journalapp.implementation;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.journalapp.response.Weather;
import com.journalapp.service.WeatherService;
import com.journalapp.utils.Logger1;

@Service
public class WeatherImplementation implements WeatherService {

	Logger1 logger;

	private static final String API_KEY = "c6fcc64a0f20c0e5cf7f5de873719b94";

	private RestTemplate restTemplate;

	public WeatherImplementation(Logger1 logger, RestTemplate restTemplate) {
		this.logger = logger;
		this.restTemplate = restTemplate;
	}

	public ResponseEntity<Object> getWeather(String city) {
		String api = "https://api.weatherstack.com/current?access_key=API_KEY&query=CITY";
		api = api.replace("API_KEY", API_KEY).replace("CITY", city);
		ResponseEntity<Weather> response = restTemplate.exchange(api, HttpMethod.GET, null, Weather.class);
		return new ResponseEntity<>(response.getBody(), HttpStatus.ACCEPTED);
	}
}