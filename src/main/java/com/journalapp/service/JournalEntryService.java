package com.journalapp.service;

import org.springframework.http.ResponseEntity;

import com.journalapp.model.JournalEntry;

public interface JournalEntryService {
	public ResponseEntity<Object> createEntry(JournalEntry journalEntry);

	public ResponseEntity<Object> getAllJournals();

	public ResponseEntity<Object> getJournalById(String id);

	public ResponseEntity<Object> updateJournalEntryById(JournalEntry journalEntry);

	public ResponseEntity<Object> deleteJournalById(String id);
}
