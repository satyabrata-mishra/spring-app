package com.journalapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.journalapp.implementation.JournalEntryServiceImplementation;
import com.journalapp.model.JournalEntry;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

	JournalEntryServiceImplementation journalEntryService;

	public JournalEntryController(JournalEntryServiceImplementation journalEntryService) {
		this.journalEntryService = journalEntryService;
	}

	@PostMapping("/createJournal")
	public ResponseEntity<Object> createEntry(@RequestBody JournalEntry journalEntry) {
		return journalEntryService.createEntry(journalEntry);
	}

	@GetMapping("/getAllJournal")
	public ResponseEntity<Object> getAllJournals() {
		return journalEntryService.getAllJournals();
	}

	@GetMapping("getJournalByid/{id}")
	public ResponseEntity<Object> getJournalById(@PathVariable String id) {
		return journalEntryService.getJournalById(id);
	}

	@DeleteMapping("/deleteJournalByid/{id}")
	public ResponseEntity<Object> deleteJournalById(@PathVariable String id) {
		return journalEntryService.deleteJournalById(id);
	}

	@PutMapping("/updateJournalByid")
	public ResponseEntity<Object> updateJournalById(@RequestBody JournalEntry journalEntry) {
		return journalEntryService.updateJournalEntryById(journalEntry);
	}
}
