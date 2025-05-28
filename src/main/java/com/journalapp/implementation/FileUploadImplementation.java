package com.journalapp.implementation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.journalapp.service.FileUploadService;
import com.journalapp.utils.Logger1;

@Service
public class FileUploadImplementation implements FileUploadService {

	Logger1 logger;

	public FileUploadImplementation(Logger1 logger) {
		this.logger = logger;
	}

	@Override
	public ResponseEntity<Object> createUser(MultipartFile file) {
		long start = System.currentTimeMillis();
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body(List.of("No file uploaded!"));
		}
		try {
			InputStream inputStream = file.getInputStream();
			List<String> fileContent = parseFileContent(inputStream);
			int i = 0;
			for (String str : fileContent) {
				logger.info(++i + "");
				processContent(str);
			}
			return new ResponseEntity<>(fileContent.size(), HttpStatus.OK);
		} catch (IOException e) {
			return ResponseEntity.status(500).body(List.of("File upload or parsing failed: " + e.getMessage()));
		} finally {
			logger.info(
					"Total time taken inside crate user file upload " + (System.currentTimeMillis() - start) + "ms.");
		}
	}

	private List<String> parseFileContent(InputStream inputStream) throws IOException {
		List<String> fileContent = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = reader.readLine()) != null) {
				fileContent.add(line);
			}
		}
		return fileContent;
	}

	private void processContent(String str) {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}