package com.SpringDemo1.SpringDemo1.service.implementation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.SpringDemo1.SpringDemo1.model.Account;
import com.SpringDemo1.SpringDemo1.repository.SignUpJPARepository;
import com.SpringDemo1.SpringDemo1.service.SignUpService;
import com.SpringDemo1.SpringDemo1.utils.*;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Service
public class SignUpServiceImplementation implements SignUpService {

	@Autowired
	private SignUpJPARepository signUpJPARepository;

	private Constants constants = new Constants();

	@Override
	public ResponseEntity<?> createUser(Account a) {
		try {
			if (a.getEmail() == null || a.getEmail().isBlank()) {
				return new ResponseEntity<>("Email cannot be blank.", HttpStatus.INTERNAL_SERVER_ERROR);
			}

			if (!a.getEmail().matches(constants.EMAIL_REGEX)) {
				return new ResponseEntity<>("Email format is not valid.", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			List<String> allEmails = signUpJPARepository.findAllEmails();
			if (allEmails.contains(a.getEmail())) {
				return new ResponseEntity<>("Email already exsits.", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			Account response = signUpJPARepository.save(a);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (Exception e) {
			constants.display(e.getMessage(), e);
			return new ResponseEntity<>("Internal server error.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<?> getAllUser(int page, int size) {
		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<Account> usersPage = signUpJPARepository.findAll(pageable);
			return new ResponseEntity<>(usersPage, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			constants.display(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while fetching all users.");
		}
	}

	@Override
	public ResponseEntity<?> deleteUser(Account account) {
		try {
			if (account.getEmail().isEmpty() || account.getEmail().isBlank()) {
				return ResponseEntity.badRequest().body("Email cannot be empty or blank");
			}
			Account deleteUser = signUpJPARepository.findByEmail(account.getEmail());
			if (deleteUser == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("User with email " + account.getEmail() + " not found.");
			}
			deleteUser.setIsUserAccountActive(false);
			signUpJPARepository.save(deleteUser);
			return new ResponseEntity<>(deleteUser.getEmail() + " deleted successfully.", HttpStatus.ACCEPTED);
		} catch (Exception e) {
			constants.display(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while deleting user: ");
		}
	}

	@Override
	public ResponseEntity<?> loginUser(Account account) {
		try {
			Account acc = signUpJPARepository.findByEmail(account.getEmail());
			if (acc == null) {
				return new ResponseEntity<>("User doesn't exsits.", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			if (acc.getLoginFailedAttempts() >= 5) {
				return new ResponseEntity<>("User account is locked due to too many failed login attempts.",
						HttpStatusCode.valueOf(423));
			}
			if (account.getPassword().isBlank() || account.getPassword().isEmpty()) {
				return new ResponseEntity<>("Password field cannot be blank.", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			if (!acc.getIsPasswordChanged()) {
				String password = account.getPassword();
				password = hashPassword(password);
				acc.setPassword(password);
				acc.setIsPasswordChanged(true);
				acc.setLastLogin(LocalDateTime.now());
				acc.setIsUserAccountActive(true);
				signUpJPARepository.save(acc);
				return new ResponseEntity<>(acc, HttpStatus.OK);
			}
			if (!acc.getIsUserAccountActive()) {
				return new ResponseEntity<>("User account is either deactivated or has been deleted.",
						HttpStatusCode.valueOf(403));
			}
			if (!verifyPassword(account.getPassword(), acc.getPassword())) {
				acc.setLoginFailedAttempts(acc.getLoginFailedAttempts() + 1);
				signUpJPARepository.save(acc);
				return new ResponseEntity<>("Login denied, wrong password", HttpStatusCode.valueOf(401));
			}
			acc.setLastLogin(LocalDateTime.now());
			acc.setLoginFailedAttempts(0);
			signUpJPARepository.save(acc);
			return new ResponseEntity<>(acc, HttpStatus.OK);
		} catch (Exception e) {
			constants.display(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while deleting user: ");
		}
	}

	public String hashPassword(String plainPassword) {
		String result = BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray());
		return result;
	}

	public boolean verifyPassword(String plainPassword, String hashedPassword) {
		BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword);
		return result.verified;
	}
}