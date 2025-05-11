package com.SpringDemo1.SpringDemo1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SpringDemo1.SpringDemo1.model.JournalEntry;
import com.SpringDemo1.SpringDemo1.service.implementation.JournalEntryServiceImplementation;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

	@Autowired
	JournalEntryServiceImplementation journalEntryService;

	@PostMapping("/createJournal")
	public ResponseEntity<?> createEntry(@RequestBody JournalEntry journalEntry) {
		return journalEntryService.createEntry(journalEntry);
	}

	@GetMapping("/getAllJournal")
	public ResponseEntity<?> getAllJournals() {
		return journalEntryService.getAllJournals();
	}

	@GetMapping("getJournalByid/{id}")
	public ResponseEntity<?> getJournalById(@PathVariable String id) {
		return journalEntryService.getJournalById(id);
	}

	@DeleteMapping("/deleteJournalByid/{id}")
	public ResponseEntity<?> deleteJournalById(@PathVariable String id) {
		return journalEntryService.deleteJournalById(id);
	}

	@PutMapping("/updateJournalByid")
	public ResponseEntity<?> updateJournalById(@RequestBody JournalEntry journalEntry) {
		return journalEntryService.updateJournalEntryById(journalEntry);
	}
}
