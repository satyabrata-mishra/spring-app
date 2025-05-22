package com.journalapp.implementation;

import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.journalapp.model.OtpVerification;
import com.journalapp.repository.OtpJPARepository;
import com.journalapp.utils.Logger1;

@Service
public class EmailService {

	JavaMailSender javaMailSender;

	Logger1 logger;

	OtpJPARepository otpJPARepository;

	private static final Random RANDOM = new Random();

	public EmailService(JavaMailSender javaMailSender, Logger1 logger, OtpJPARepository otpJPARepository) {
		this.javaMailSender = javaMailSender;
		this.logger = logger;
		this.otpJPARepository = otpJPARepository;
	}

	public void sendEmail(String to, String subject, String body) {
		try {
			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			simpleMailMessage.setTo(to);
			simpleMailMessage.setSubject(subject);
			simpleMailMessage.setText(body);
			javaMailSender.send(simpleMailMessage);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	public void sendOTP(String email) {
		String subject = "Your One-Time Password (OTP) for Account Verification";
		int otp = otpGenerator();
		String body = "Your One-Time Password (OTP) is: " + otp;
		sendEmail(email, subject, body);
		otpJPARepository.save(new OtpVerification(email, Integer.toString(otp), true));
	}

	public int otpGenerator() {
		return 10000 + RANDOM.nextInt(90000);
	}
}
