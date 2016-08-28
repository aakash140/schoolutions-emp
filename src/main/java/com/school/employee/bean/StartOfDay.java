package com.school.employee.bean;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class StartOfDay {

	private Calendar workDay;
	private String employeeID;
	private LocalDateTime timestamp;
	private Date version;

	public Calendar getWorkDay() {
		return workDay;
	}

	public void setWorkDay(Calendar workDay) {
		this.workDay = workDay;
	}

	public String getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}
}