package com.SpringDemo1.SpringDemo1.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.SpringDemo1.SpringDemo1.model.Account;
import com.SpringDemo1.SpringDemo1.model.JournalEntry;
import com.SpringDemo1.SpringDemo1.repository.JournalEntryJPARepository;
import com.SpringDemo1.SpringDemo1.utils.Constants;

@Service
public class JournalEntryServiceImplementation {

	@Autowired
	JournalEntryJPARepository journalEntryRepository;

	@Autowired
	Constants utils;

	public ResponseEntity<?> createEntry(JournalEntry journalEntry) {
		try {
			List<String> emails = journalEntryRepository.findAllEmails();
			if (!emails.contains(journalEntry.getEmail())) {
				return new ResponseEntity<>("Invalid email is given thus record can't be created,",
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
			Account account = journalEntryRepository.findByEmail(journalEntry.getEmail());
			ResponseEntity<?> authorization = authorization(account);
			if (authorization.getStatusCode() != HttpStatusCode.valueOf(200)) {
				return authorization;
			}
			JournalEntry result = journalEntryRepository.save(journalEntry);
			return new ResponseEntity<>(result, HttpStatus.CREATED);
		} catch (Exception e) {
			utils.display(e.getLocalizedMessage(), e);
			return new ResponseEntity<>("Some unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> getAllJournals() {
		try {
			List<JournalEntry> result = journalEntryRepository.findAll();
			return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			utils.display(e.getLocalizedMessage(), e);
			return new ResponseEntity<>("Some unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> getJournalById(String id) {
		try {
			Optional<JournalEntry> result = journalEntryRepository.findById(id);
			if (result.isPresent()) {
				return new ResponseEntity<>(result.get(), HttpStatus.OK);
			}
			return new ResponseEntity<>("Record with id: " + id + " is not present.", HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			utils.display(e.getLocalizedMessage(), e);
			return new ResponseEntity<>("Some unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> deleteJournalById(String id) {
		try {
			Optional<JournalEntry> journalEntry = journalEntryRepository.findById(id);
			if (journalEntry.isPresent()) {
				journalEntryRepository.deleteById(id);
				return new ResponseEntity<>(journalEntry, HttpStatus.ACCEPTED);
			}
			return new ResponseEntity<>("Record with id: " + id + " is not present.", HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			utils.display(e.getLocalizedMessage(), e);
			return new ResponseEntity<>("Some unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> updateJournalEntryById(JournalEntry journalEntry) {
		try {
			Optional<JournalEntry> result = journalEntryRepository.findById(journalEntry.getId());
			if (result.isPresent()) {
				JournalEntry oldEntry = result.get();
				oldEntry.setTitle(
						journalEntry.getTitle() != null && !journalEntry.getTitle().isEmpty() ? journalEntry.getTitle()
								: oldEntry.getTitle());
				oldEntry.setContent(journalEntry.getContent() != null && !journalEntry.getContent().isEmpty()
						? journalEntry.getContent()
						: oldEntry.getContent());
				journalEntry = journalEntryRepository.save(oldEntry);
				return new ResponseEntity<>(journalEntry, HttpStatus.ACCEPTED);
			}
			return new ResponseEntity<>("Record with id: " + journalEntry.getId() + " is not present.",
					HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			utils.display(e.getLocalizedMessage(), e);
			return new ResponseEntity<>("Some unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> authorization(Account account) {
		if (!account.getIsPasswordChanged()) {
			return new ResponseEntity<>("User doesn't have permission to perform this action.",
					HttpStatus.UNAUTHORIZED);
		}
		if (!account.getIsUserAccountActive()) {
			return new ResponseEntity<>("User account is inactive.", HttpStatus.UNAUTHORIZED);
		}
		if (account.getLoginFailedAttempts() >= 5) {
			return new ResponseEntity<>("User account is locked.", HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}