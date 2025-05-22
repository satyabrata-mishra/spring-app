package com.journalapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "otpverification")
@NoArgsConstructor
@AllArgsConstructor
public class OtpVerification {

	@Id
	@Column(name = "email", unique = true, nullable = false)
	String email;

	@Column(name = "otp", nullable = false)
	String otp;

	@Column(name = "isotpvalid")
	boolean isOtpValid;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public boolean getOtpValid() {
		return isOtpValid;
	}

	public void setOtpValid(boolean isOtpValid) {
		this.isOtpValid = isOtpValid;
	}
}
