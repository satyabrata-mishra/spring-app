package com.SpringDemo1.SpringDemo1.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "journalentry")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class JournalEntry {

	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	@Column(name = "email")
	private String email;

	@Column(name = "title")
	private String title;

	@Column(name = "content")
	private String content;
	LocalDateTime date = LocalDateTime.now();
}
