package com.school.employee.bean;

import java.time.LocalDateTime;
import java.util.Date;

public class UserOTP {

	private String userID;
	private String OTP;
	private LocalDateTime OTPTimestamp;
	private int activeStatus = 1;
	private Date version;

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getOTP() {
		return OTP;
	}

	public void setOTP(String oTP) {
		OTP = oTP;
	}

	public LocalDateTime getOTPTimestamp() {
		return OTPTimestamp;
	}

	public void setOTPTimestamp(LocalDateTime oTPTimestamp) {
		OTPTimestamp = oTPTimestamp;
	}

	public int getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

}