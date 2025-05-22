package com.journalapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.journalapp.model.Account;
import com.journalapp.model.OtpVerification;
import com.journalapp.service.SignUpService;

@RestController
@RequestMapping("/signup")
public class SignUpController {

	private SignUpService signUpService;

	public SignUpController(SignUpService signUpService) {
		this.signUpService = signUpService;
	}

	@PostMapping("/createuser")
	public ResponseEntity<Object> addUser(@RequestBody Account account) {
		return signUpService.createUser(account);
	}

	@GetMapping("/getallusers")
	public ResponseEntity<Object> getAllUsers(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		return signUpService.getAllUser(page, size);
	}

	@DeleteMapping("/deleteuser")
	public ResponseEntity<Object> deleteUsers(@RequestBody Account account) {
		return signUpService.deleteUser(account);
	}

	@PutMapping("/login")
	public ResponseEntity<Object> loginUser(@RequestBody Account account) {
		return signUpService.loginUser(account);
	}

	@PostMapping("/verifuser")
	public ResponseEntity<Object> verifyUser(@RequestBody OtpVerification otp) {
		return signUpService.verifyUser(otp);
	}

	@GetMapping("/resendotp/{email}")
	public ResponseEntity<Object> resendOtp(@PathVariable String email) {
		return signUpService.resendotp(email);
	}
}
