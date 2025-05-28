package com.journalapp.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.journalapp.response.Weather;
import com.journalapp.service.WeatherService;
import com.journalapp.utils.Logger1;

import jakarta.annotation.PostConstruct;

@Service
public class WeatherImplementation implements WeatherService {
	Logger1 logger;

	@Value("${weather.api.key}")
	private String apiKey;

	private RestTemplate restTemplate;

	public WeatherImplementation(Logger1 logger, RestTemplate restTemplate) {
		this.logger = logger;
		this.restTemplate = restTemplate;
	}

	public ResponseEntity<Object> getWeather(String city) {
		String api = "https://api.weatherstack.com/current?access_key=API_KEY&query=CITY";
		api = api.replace("API_KEY", apiKey).replace("CITY", city);
		ResponseEntity<Weather> response = restTemplate.exchange(api, HttpMethod.GET, null, Weather.class);
		return new ResponseEntity<>(response.getBody(), HttpStatus.ACCEPTED);
	}

	@PostConstruct
	public void hello() {
		logger.info("Hi I am post construct");
	}
}