package com.SpringDemo1.SpringDemo1.utils;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;

@Component
public class Constants {
	public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

	private final Logger logger = LoggerFactory.getLogger(Constants.class);

	public void display(String message, Exception exp) {
		logger.error(message);
	}

	public void display(String message) {
		logger.error(message);
	}

}
