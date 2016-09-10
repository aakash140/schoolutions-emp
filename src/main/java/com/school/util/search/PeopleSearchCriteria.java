package com.school.util.search;

import java.util.Calendar;

public abstract class PeopleSearchCriteria {

	private String firstName;
	private String lastName;
	private String contactNum;
	private String gender;
	private Calendar dob;
	private Calendar doj;
	private String bloodgroup;

	public String getfirstName() {
		return firstName;
	}

	public void setfirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getlastName() {
		return lastName;
	}

	public void setlastName(String lastName) {
		this.lastName = lastName;
	}

	public String getContactNum() {
		return contactNum;
	}

	public void setContactNum(String contactNum) {
		this.contactNum = contactNum;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Calendar getDob() {
		return dob;
	}

	public void setDob(Calendar dob) {
		this.dob = dob;
	}

	public Calendar getDoj() {
		return doj;
	}

	public void setDoj(Calendar doj) {
		this.doj = doj;
	}

	public String getBloodgroup() {
		return bloodgroup;
	}

	public void setBloodgroup(String bloodgroup) {
		this.bloodgroup = bloodgroup;
	}

}