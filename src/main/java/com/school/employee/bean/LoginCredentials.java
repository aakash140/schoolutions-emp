package com.school.employee.bean;

import java.time.LocalDateTime;

public class LoginCredentials {

	private String userID;
	private String password;
	private int failedAttempts;
	private LocalDateTime lastLoginTimestamp;

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getFailedAttempts() {
		return failedAttempts;
	}

	public void setFailedAttempts(int failedAttempts) {
		this.failedAttempts = failedAttempts;
	}

	public LocalDateTime getLastLoginTimestamp() {
		return lastLoginTimestamp;
	}

	public void setLastLoginTimestamp(LocalDateTime lastLoginTimestamp) {
		this.lastLoginTimestamp = lastLoginTimestamp;
	}
}
