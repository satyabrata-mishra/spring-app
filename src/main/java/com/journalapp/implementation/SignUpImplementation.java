package com.journalapp.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.journalapp.model.Account;
import com.journalapp.model.OtpVerification;
import com.journalapp.repository.OtpJPARepository;
import com.journalapp.repository.SignUpJPARepository;
import com.journalapp.service.SignUpService;
import com.journalapp.utils.*;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Service
public class SignUpImplementation implements SignUpService {

	private SignUpJPARepository signUpJPARepository;

	private OtpJPARepository otpJpaRepository;

	private Logger1 logger;

	private EmailService emailService;

	public SignUpImplementation(SignUpJPARepository signUpJPARepository, Logger1 logger, EmailService emailService,
			OtpJPARepository otpJpaRepository) {
		this.signUpJPARepository = signUpJPARepository;
		this.logger = logger;
		this.emailService = emailService;
		this.otpJpaRepository = otpJpaRepository;
	}

	@Override
	public ResponseEntity<Object> createUser(Account a) {
		try {
			if (a.getEmail() == null || a.getEmail().isBlank()) {
				return new ResponseEntity<>("Email cannot be blank.", HttpStatus.BAD_REQUEST);
			}

			if (!a.getEmail().matches(Constants.EMAIL_REGEX)) {
				return new ResponseEntity<>("Email format is not valid.", HttpStatus.BAD_REQUEST);
			}
			List<String> allEmails = signUpJPARepository.findAllEmails();
			if (allEmails.contains(a.getEmail())) {
				return new ResponseEntity<>("Email already exists during registration.", HttpStatus.CONFLICT);
			}
			emailService.sendOTP(a.getEmail());
			Account response = signUpJPARepository.save(a);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<>(Constants.UNEXPECTED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<Object> getAllUser(int page, int size) {
		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<Account> usersPage = signUpJPARepository.findAll(pageable);
			return new ResponseEntity<>(usersPage, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<>(Constants.UNEXPECTED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<Object> deleteUser(Account account) {
		try {
			if (account.getEmail().isEmpty() || account.getEmail().isBlank()) {
				return new ResponseEntity<>("Email cannot be blank.", HttpStatus.BAD_REQUEST);
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
			logger.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while deleting user: ");
		}
	}

	@Override
	public ResponseEntity<Object> loginUser(Account account) {
		try {
			Account acc = signUpJPARepository.findByEmail(account.getEmail());
			if (acc == null) {
				return new ResponseEntity<>("User doesn't exsits.", HttpStatus.NOT_FOUND);
			}
			if (!acc.getUserVerified()) {
				return new ResponseEntity<>("User is not verified.", HttpStatus.FORBIDDEN);
			}
			if (acc.getLoginFailedAttempts() >= 5) {
				return new ResponseEntity<>("User account is locked due to too many failed login attempts.",
						HttpStatus.LOCKED);
			}
			if (account.getPassword().isBlank() || account.getPassword().isEmpty()) {
				return new ResponseEntity<>("Password field cannot be blank.", HttpStatus.BAD_REQUEST);
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
						HttpStatus.FORBIDDEN);
			}
			if (!verifyPassword(account.getPassword(), acc.getPassword())) {
				acc.setLoginFailedAttempts(acc.getLoginFailedAttempts() + 1);
				signUpJPARepository.save(acc);
				return new ResponseEntity<>("Login denied, wrong password", HttpStatus.FORBIDDEN);
			}
			acc.setLastLogin(LocalDateTime.now());
			acc.setLoginFailedAttempts(0);
			signUpJPARepository.save(acc);
			return new ResponseEntity<>(acc, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while deleting user: ");
		}
	}

	@Override
	public ResponseEntity<Object> verifyUser(OtpVerification otp) {
		try {
			Optional<OtpVerification> otpObject = otpJpaRepository.findById(otp.getEmail());
			Optional<Account> acc = signUpJPARepository.findById(otp.getEmail());
			if (!acc.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("User with email " + otp.getEmail() + " not found.");
			}
			if (!otpObject.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("User with email " + otp.getEmail() + " not found.");
			}
			OtpVerification response = otpObject.get();
			Account account = acc.get();
			if (account.getUserVerified()) {
				return new ResponseEntity<>("User is already verified", HttpStatus.OK);
			}
			if (!response.getOtp().equals(otp.getOtp())) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Wrong OTP");
			}
			response.setOtpValid(false);
			otpJpaRepository.save(response);
			account.setUserVerified(true);
			signUpJPARepository.save(account);
			return new ResponseEntity<>("Verification successfull", HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>("Internal server error.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<Object> resendotp(String email) {
		try {
			emailService.sendOTP(email);
			return new ResponseEntity<>("Otp sent successfully.", HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>("Internal server error.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public String hashPassword(String plainPassword) {
		return BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray());
	}

	public boolean verifyPassword(String plainPassword, String hashedPassword) {
		BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword);
		return result.verified;
	}
}