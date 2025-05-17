package com.journalapp.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accountdeatils")
@NoArgsConstructor
@AllArgsConstructor
public class Account {

	@Column(name = "name")
	String name;

	@Id
	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "password")
	private String password = "Welcome@123";

	@Column(name = "ispasswordchanged")
	private boolean isPasswordChanged = false;

	@Column(name = "isuseraccountactive")
	private boolean isUserAccountActive = false;

	@Column(name = "createdat", updatable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(name = "updatedat")
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@Column(name = "lastlogin")
	private LocalDateTime lastLogin;

	@Column(name = "loginfailedattempts")
	private int loginFailedAttempts = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getIsPasswordChanged() {
		return isPasswordChanged;
	}

	public void setIsPasswordChanged(boolean isPasswordChanged) {
		this.isPasswordChanged = isPasswordChanged;
	}

	public boolean getIsUserAccountActive() {
		return isUserAccountActive;
	}

	public void setIsUserAccountActive(boolean isUserAccountActive) {
		this.isUserAccountActive = isUserAccountActive;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public LocalDateTime getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(LocalDateTime lastLogin) {
		this.lastLogin = lastLogin;
	}

	public int getLoginFailedAttempts() {
		return loginFailedAttempts;
	}

	public void setLoginFailedAttempts(int loginFailedAttempts) {
		this.loginFailedAttempts = loginFailedAttempts;
	}

}
