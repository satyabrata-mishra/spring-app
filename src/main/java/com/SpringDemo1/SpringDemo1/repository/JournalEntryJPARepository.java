package com.SpringDemo1.SpringDemo1.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.SpringDemo1.SpringDemo1.model.Account;
import com.SpringDemo1.SpringDemo1.model.JournalEntry;

@Repository
public interface JournalEntryJPARepository extends JpaRepository<JournalEntry, String> {

	@Query(value = "select email from accountdeatils", nativeQuery = true)
	List<String> findAllEmails();

	@Query(value = "SELECT * FROM accountdeatils WHERE email = ?1", nativeQuery = true)
	Account findByEmail(String email);
}
