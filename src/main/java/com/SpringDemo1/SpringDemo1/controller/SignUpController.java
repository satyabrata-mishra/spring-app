package com.SpringDemo1.SpringDemo1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SpringDemo1.SpringDemo1.model.Account;
import com.SpringDemo1.SpringDemo1.service.SignUpService;

@RestController
@RequestMapping("/signup")
public class SignUpController {

	@Autowired
	private SignUpService signUpService;

	@PostMapping("/createuser")
	public ResponseEntity<?> addUser(@RequestBody Account account) {
		return signUpService.createUser(account);
	}

	@GetMapping("/getallusers")
	public ResponseEntity<?> getAllUsers(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		return signUpService.getAllUser(page, size);
	}

	@DeleteMapping("/deleteuser")
	public ResponseEntity<?> deleteUsers(@RequestBody Account account) {
		return signUpService.deleteUser(account);
	}

	@PutMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody Account account) {
		return signUpService.loginUser(account);
	}
}
