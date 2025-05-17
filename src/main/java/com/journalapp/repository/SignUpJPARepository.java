package com.journalapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.journalapp.model.Account;

public interface SignUpJPARepository extends JpaRepository<Account, String> {
	@Query(value = "SELECT email FROM accountdeatils", nativeQuery = true)
	List<String> findAllEmails();

	@Query(value = "SELECT * FROM accountdeatils WHERE email = ?1", nativeQuery = true)
	Account findByEmail(String email);
}
