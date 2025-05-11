package com.SpringDemo1.SpringDemo1.service;

import org.springframework.http.ResponseEntity;

import com.SpringDemo1.SpringDemo1.model.Account;

public interface SignUpService {
	ResponseEntity<?> createUser(Account account);

	ResponseEntity<?> getAllUser(int page, int size);

	ResponseEntity<?> deleteUser(Account account);

	ResponseEntity<?> loginUser(Account account);
}
