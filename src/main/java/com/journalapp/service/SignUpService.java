package com.journalapp.service;

import org.springframework.http.ResponseEntity;

import com.journalapp.model.Account;
import com.journalapp.model.OtpVerification;

public interface SignUpService {
	ResponseEntity<Object> createUser(Account account);

	ResponseEntity<Object> getAllUser(int page, int size);

	ResponseEntity<Object> deleteUser(Account account);

	ResponseEntity<Object> loginUser(Account account);

	ResponseEntity<Object> verifyUser(OtpVerification otp);

	ResponseEntity<Object> resendotp(String email);
}
