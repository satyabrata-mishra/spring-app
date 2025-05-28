package com.journalapp.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.journalapp.utils.Logger1;

@Component
public class UserScheduler {
	Logger1 logger;

	public UserScheduler(Logger1 logger) {
		this.logger = logger;
	}

	@Scheduled(cron = "*/30 * * * * *")
	public void scheduler() {
		logger.info("I am a cron job which runs every 30 sec.");
	}
}
