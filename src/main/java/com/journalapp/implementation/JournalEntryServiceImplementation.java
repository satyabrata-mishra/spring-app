package com.journalapp.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.journalapp.model.Account;
import com.journalapp.model.JournalEntry;
import com.journalapp.repository.JournalEntryJPARepository;
import com.journalapp.utils.Constants;
import com.journalapp.utils.Logger1;

@Service
public class JournalEntryServiceImplementation {

	JournalEntryJPARepository journalEntryRepository;

	Logger1 logger;

	public JournalEntryServiceImplementation(JournalEntryJPARepository journalEntryRepository, Logger1 logger) {
		this.journalEntryRepository = journalEntryRepository;
		this.logger = logger;
	}

	public ResponseEntity<Object> createEntry(JournalEntry journalEntry) {
		try {
			List<String> emails = journalEntryRepository.findAllEmails();
			if (!emails.contains(journalEntry.getEmail())) {
				return new ResponseEntity<>("Invalid email is given thus record can't be created,",
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
			Account account = journalEntryRepository.findByEmail(journalEntry.getEmail());
			ResponseEntity<?> authorization = authorization(account);
			if (authorization.getStatusCode() != HttpStatusCode.valueOf(200)) {
				return new ResponseEntity<>(authorization, HttpStatus.UNAUTHORIZED);
			}
			JournalEntry result = journalEntryRepository.save(journalEntry);
			return new ResponseEntity<>(result, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(Constants.UNEXPECTED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Object> getAllJournals() {
		try {
			List<JournalEntry> result = journalEntryRepository.findAll();
			return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(Constants.UNEXPECTED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Object> getJournalById(String id) {
		try {
			Optional<JournalEntry> result = journalEntryRepository.findById(id);
			if (result.isPresent()) {
				return new ResponseEntity<>(result.get(), HttpStatus.OK);
			}
			return new ResponseEntity<>("Record with id: " + id + " is not present.", HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(Constants.UNEXPECTED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Object> deleteJournalById(String id) {
		try {
			Optional<JournalEntry> journalEntry = journalEntryRepository.findById(id);
			if (journalEntry.isPresent()) {
				journalEntryRepository.deleteById(id);
				return new ResponseEntity<>(journalEntry, HttpStatus.ACCEPTED);
			}
			return new ResponseEntity<>("Record with id: " + id + " is not present.", HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(Constants.UNEXPECTED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Object> updateJournalEntryById(JournalEntry journalEntry) {
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
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(Constants.UNEXPECTED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Object> authorization(Account account) {
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