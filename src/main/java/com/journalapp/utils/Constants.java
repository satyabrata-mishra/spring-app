package com.journalapp.utils;

public class Constants {
	public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

	public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

	public static final String UNEXPECTED_ERROR = "Some unexpected error occurred.";

	private Constants() {

	}
}
