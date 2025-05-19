package com.journalapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import com.journalapp.implementation.SignUpImplementation;
import com.journalapp.model.Account;
import com.journalapp.repository.SignUpJPARepository;

@SpringBootTest
@Transactional
class SignUpServiceTests {

	@Autowired
	private SignUpImplementation signUpService;

	@Autowired
	private SignUpJPARepository repo;

	@Test
	void testCreateUser_success() {
		Account acc = new Account();
		acc.setEmail("test@example.com");
		acc.setName("Test User");
		acc.setPassword("password123");
		ResponseEntity<Object> response = signUpService.createUser(acc);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	void testCreateUser_blankEmail() {
		Account acc = new Account();
		acc.setEmail(" ");
		ResponseEntity<Object> response = signUpService.createUser(acc);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	void testCreateUser_invalidEmail() {
		Account acc = new Account();
		acc.setEmail("invalid-email");
		ResponseEntity<Object> response = signUpService.createUser(acc);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	void testCreateUser_duplicateEmail() {
		Account acc = new Account();
		acc.setEmail("duplicate@example.com");
		acc.setName("Original");
		acc.setPassword("pass");
		repo.save(acc);

		Account duplicate = new Account();
		duplicate.setEmail("duplicate@example.com");
		duplicate.setName("Copy");
		duplicate.setPassword("pass2");
		ResponseEntity<Object> response = signUpService.createUser(duplicate);
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
	}

	@Test
	void testGetAllUsers() {
		ResponseEntity<Object> response = signUpService.getAllUser(0, 10);
		assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
	}

	@Test
	void testDeleteUser_success() {
		Account acc = new Account();
		acc.setEmail("delete@test.com");
		acc.setName("Delete Me");
		acc.setPassword("pass");
		acc.setIsUserAccountActive(true);
		repo.save(acc);

		ResponseEntity<Object> response = signUpService.deleteUser(acc);
		assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
	}

	@Test
	void testDeleteUser_nonExistent() {
		Account acc = new Account();
		acc.setEmail("notfound@example.com");
		ResponseEntity<Object> response = signUpService.deleteUser(acc);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void testDeleteUser_blankEmail() {
		Account acc = new Account();
		acc.setEmail(" ");
		ResponseEntity<Object> response = signUpService.deleteUser(acc);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	void testLoginUser_firstTimePasswordChange() {
		Account acc = new Account();
		acc.setEmail("firstlogin@example.com");
		acc.setPassword("temp123");
		acc.setIsPasswordChanged(false);
		acc.setIsUserAccountActive(true);
		acc.setLoginFailedAttempts(0);
		repo.save(acc);

		Account login = new Account();
		login.setEmail("firstlogin@example.com");
		login.setPassword("temp123");
		ResponseEntity<Object> response = signUpService.loginUser(login);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void testLoginUser_accountLocked() {
		Account acc = new Account();
		acc.setEmail("locked@example.com");
		acc.setPassword("pass");
		acc.setLoginFailedAttempts(5);
		acc.setIsPasswordChanged(true);
		acc.setIsUserAccountActive(true);
		repo.save(acc);

		ResponseEntity<Object> response = signUpService.loginUser(acc);
		assertEquals(HttpStatus.LOCKED, response.getStatusCode());
	}

	@Test
	void testLoginUser_wrongPassword() {
		Account acc = new Account();
		acc.setEmail("wrongpass@example.com");
		acc.setPassword(signUpService.hashPassword("correctpass"));
		acc.setLoginFailedAttempts(0);
		acc.setIsPasswordChanged(true);
		acc.setIsUserAccountActive(true);
		repo.save(acc);

		Account login = new Account();
		login.setEmail("wrongpass@example.com");
		login.setPassword("wrongpass");

		ResponseEntity<Object> response = signUpService.loginUser(login);
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
	}

	@Test
	void testLoginUser_inactiveAccount() {
		Account acc = new Account();
		acc.setEmail("inactive@example.com");
		acc.setPassword(signUpService.hashPassword("mypassword"));
		acc.setLoginFailedAttempts(0);
		acc.setIsPasswordChanged(true);
		acc.setIsUserAccountActive(false);
		repo.save(acc);

		Account login = new Account();
		login.setEmail("inactive@example.com");
		login.setPassword("mypassword");

		ResponseEntity<Object> response = signUpService.loginUser(login);
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
	}

	@Test
	void testLoginUser_success() {
		Account acc = new Account();
		acc.setEmail("login@example.com");
		acc.setPassword(signUpService.hashPassword("secret123"));
		acc.setLoginFailedAttempts(0);
		acc.setIsPasswordChanged(true);
		acc.setIsUserAccountActive(true);
		repo.save(acc);

		Account login = new Account();
		login.setEmail("login@example.com");
		login.setPassword("secret123");

		ResponseEntity<Object> response = signUpService.loginUser(login);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void testLoginUser_notFound() {
		Account acc = new Account();
		acc.setEmail("nouser@example.com");
		acc.setPassword("pass");

		ResponseEntity<Object> response = signUpService.loginUser(acc);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void testLoginUser_blankPassword() {
		Account acc = new Account();
		acc.setEmail("blankpass@example.com");
		acc.setPassword(signUpService.hashPassword("realpass"));
		acc.setIsPasswordChanged(true);
		acc.setIsUserAccountActive(true);
		acc.setLoginFailedAttempts(0);
		repo.save(acc);

		Account login = new Account();
		login.setEmail("blankpass@example.com");
		login.setPassword(" ");

		ResponseEntity<Object> response = signUpService.loginUser(login);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
}
