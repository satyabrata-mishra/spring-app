package com.journalapp.utils;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;

@Component
public class Logger1 {

	private static final Logger logger = LoggerFactory.getLogger(Logger1.class);

	public void error(String message, Exception exp) {
		logger.error(message, exp);
	}

	public void info(String message) {
		logger.info(message);
	}

}
