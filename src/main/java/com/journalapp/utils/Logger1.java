package com.journalapp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Logger1 {

	public void error(String message, Exception exp) {
		Logger logger = getCallerLogger();
		logger.error(message, exp);
	}

	public void info(String message) {
		Logger logger = getCallerLogger();
		logger.info(message);
	}

	private Logger getCallerLogger() {
		// Get the current thread's stack trace
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

		// stackTrace[0] = Thread.getStackTrace
		// stackTrace[1] = this method (getCallerLogger)
		// stackTrace[2] = info() or error()
		// stackTrace[3] = caller of info()/error() — that's what we want
		for (int i = 2; i < stackTrace.length; i++) {
			String className = stackTrace[i].getClassName();
			if (!className.equals(Logger1.class.getName())) {
				try {
					Class<?> callerClass = Class.forName(className);
					return LoggerFactory.getLogger(callerClass);
				} catch (ClassNotFoundException e) {
					// fallback
					break;
				}
			}
		}
		// fallback logger
		return LoggerFactory.getLogger(Logger1.class);
	}
}
