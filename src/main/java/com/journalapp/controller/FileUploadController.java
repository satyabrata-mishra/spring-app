package com.journalapp.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.journalapp.implementation.FileUploadImplementation;

@RestController
@RequestMapping("/file-upload")
public class FileUploadController {

	FileUploadImplementation fileUpload;

	public FileUploadController(FileUploadImplementation fileUpload) {
		this.fileUpload = fileUpload;
	}

	@PostMapping
	public ResponseEntity<Object> uploadAndParseFile(@RequestParam("file") MultipartFile file) {
		return fileUpload.createUser(file);
	}
}