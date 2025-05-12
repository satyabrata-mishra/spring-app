package com.SpringDemo1.SpringDemo1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import com.SpringDemo1.SpringDemo1.model.Account;
import com.SpringDemo1.SpringDemo1.repository.SignUpJPARepository;
import com.SpringDemo1.SpringDemo1.service.implementation.SignUpServiceImplementation;

@SpringBootTest
@Transactional
public class SignUpServiceTests {

	@Autowired
	private SignUpServiceImplementation signUpService;

	@Autowired
	private SignUpJPARepository repo;

	@Test
	public void testCreateUser_success() {
		Account acc = new Account();
		acc.setEmail("test@example.com");
		acc.setName("Test User");
		acc.setPassword("password123");
		ResponseEntity<?> response = signUpService.createUser(acc);
		assertEquals(201, response.getStatusCodeValue());
	}

	@Test
	public void testCreateUser_blankEmail() {
		Account acc = new Account();
		acc.setEmail(" ");
		ResponseEntity<?> response = signUpService.createUser(acc);
		assertEquals(400, response.getStatusCodeValue());
	}

	@Test
	public void testCreateUser_invalidEmail() {
		Account acc = new Account();
		acc.setEmail("invalid-email");
		ResponseEntity<?> response = signUpService.createUser(acc);
		assertEquals(400, response.getStatusCodeValue());
	}

	@Test
	public void testCreateUser_duplicateEmail() {
		Account acc = new Account();
		acc.setEmail("duplicate@example.com");
		acc.setName("Original");
		acc.setPassword("pass");
		repo.save(acc);

		Account duplicate = new Account();
		duplicate.setEmail("duplicate@example.com");
		duplicate.setName("Copy");
		duplicate.setPassword("pass2");
		ResponseEntity<?> response = signUpService.createUser(duplicate);
		assertEquals(409, response.getStatusCodeValue());
	}

	@Test
	public void testGetAllUsers() {
		ResponseEntity<?> response = signUpService.getAllUser(0, 10);
		assertEquals(202, response.getStatusCodeValue());
	}

	@Test
	public void testDeleteUser_success() {
		Account acc = new Account();
		acc.setEmail("delete@test.com");
		acc.setName("Delete Me");
		acc.setPassword("pass");
		acc.setIsUserAccountActive(true);
		repo.save(acc);

		ResponseEntity<?> response = signUpService.deleteUser(acc);
		assertEquals(202, response.getStatusCodeValue());
	}

	@Test
	public void testDeleteUser_nonExistent() {
		Account acc = new Account();
		acc.setEmail("notfound@example.com");
		ResponseEntity<?> response = signUpService.deleteUser(acc);
		assertEquals(404, response.getStatusCodeValue());
	}

	@Test
	public void testDeleteUser_blankEmail() {
		Account acc = new Account();
		acc.setEmail(" ");
		ResponseEntity<?> response = signUpService.deleteUser(acc);
		assertEquals(400, response.getStatusCodeValue());
	}

	@Test
	public void testLoginUser_firstTimePasswordChange() {
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
		ResponseEntity<?> response = signUpService.loginUser(login);
		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	public void testLoginUser_accountLocked() {
		Account acc = new Account();
		acc.setEmail("locked@example.com");
		acc.setPassword("pass");
		acc.setLoginFailedAttempts(5);
		acc.setIsPasswordChanged(true);
		acc.setIsUserAccountActive(true);
		repo.save(acc);

		ResponseEntity<?> response = signUpService.loginUser(acc);
		assertEquals(423, response.getStatusCodeValue());
	}

	@Test
	public void testLoginUser_wrongPassword() {
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

		ResponseEntity<?> response = signUpService.loginUser(login);
		assertEquals(403, response.getStatusCodeValue());
	}

	@Test
	public void testLoginUser_inactiveAccount() {
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

		ResponseEntity<?> response = signUpService.loginUser(login);
		assertEquals(403, response.getStatusCodeValue());
	}

	@Test
	public void testLoginUser_success() {
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

		ResponseEntity<?> response = signUpService.loginUser(login);
		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	public void testLoginUser_notFound() {
		Account acc = new Account();
		acc.setEmail("nouser@example.com");
		acc.setPassword("pass");

		ResponseEntity<?> response = signUpService.loginUser(acc);
		assertEquals(404, response.getStatusCodeValue());
	}

	@Test
	public void testLoginUser_blankPassword() {
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

		ResponseEntity<?> response = signUpService.loginUser(login);
		assertEquals(400, response.getStatusCodeValue());
	}
}
