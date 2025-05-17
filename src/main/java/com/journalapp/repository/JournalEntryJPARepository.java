package com.journalapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.journalapp.model.Account;
import com.journalapp.model.JournalEntry;

@Repository
public interface JournalEntryJPARepository extends JpaRepository<JournalEntry, String> {

	@Query(value = "select email from accountdeatils", nativeQuery = true)
	List<String> findAllEmails();

	@Query(value = "SELECT * FROM accountdeatils WHERE email = ?1", nativeQuery = true)
	Account findByEmail(String email);
}
