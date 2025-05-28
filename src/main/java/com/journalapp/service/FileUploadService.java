package com.journalapp.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

	public ResponseEntity<Object> createUser(MultipartFile file);

}
