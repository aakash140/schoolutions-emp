package com.school.employee.bean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class EmployeeAttendance implements Serializable {

	private static final long serialVersionUID = -8899418265470841311L;
	private Calendar workDay;
	private String employeeID;
	private String status;
	private LocalDateTime timestamp;
	private Date version;

	public EmployeeAttendance() {
	}

	public EmployeeAttendance(Calendar workDay, String employeeID) {
		this.workDay = workDay;
		this.employeeID = employeeID;
	}

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null || (this.getClass() != obj.getClass()))
			return false;

		EmployeeAttendance atndnc = (EmployeeAttendance) obj;

		boolean isworkdayequal = getWorkDay() == atndnc.getWorkDay()
				|| (getWorkDay() != null && getWorkDay().compareTo(atndnc.getWorkDay()) == 0);

		boolean isEmpIDEqual = getEmployeeID() == atndnc.getEmployeeID()
				|| (getEmployeeID() != null && getEmployeeID().equalsIgnoreCase(atndnc.getEmployeeID()));

		boolean isStatusEqual = getStatus() == atndnc.getStatus()
				|| (getStatus() != null && getStatus().equals(atndnc.getStatus()));

		boolean isTmpstmpEqual = getTimestamp() == atndnc.getTimestamp()
				|| (getTimestamp() != null && getTimestamp().compareTo(atndnc.getTimestamp()) == 0);

		if (isworkdayequal && isEmpIDEqual && isStatusEqual && isTmpstmpEqual)
			return true;

		return false;
	}

	@Override
	public int hashCode() {
		int hash = 7;

		hash = 31 * hash + (getEmployeeID() == null ? 0 : getEmployeeID().hashCode());
		hash = 31 * hash + (getStatus() == null ? 0 : getStatus().hashCode());

		return hash;
	}
}
