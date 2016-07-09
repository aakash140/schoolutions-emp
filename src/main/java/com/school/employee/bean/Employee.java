package com.school.employee.bean;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class Employee {

	private String empID;
	private String firstName;
	private String middleName;
	private String lastName;
	private String designation;
	private String employeeType;
	private int department;
	private String gender;
	private String bloodGroup;
	private Calendar DOB;
	private Calendar DOJ;
	private String nameOfFather;
	private String nameOfMother;
	private String maritalStatus;
	private String nameOfSpouse;
	private String nationalityID;
	private String PANnumber;
	private float experience;
	private ContactDetails contact;
	private Set<Address> addressSet;
	private int status = 1;
	private Date version;

	public String getEmpID() {
		return empID;
	}

	public void setEmpID(String empID) {
		this.empID = empID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public int getDepartment() {
		return department;
	}

	public void setDepartment(int department) {
		this.department = department;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public Calendar getDOB() {
		return DOB;
	}

	public void setDOB(Calendar dOB) {
		DOB = dOB;
	}

	public Calendar getDOJ() {
		return DOJ;
	}

	public void setDOJ(Calendar dOJ) {
		DOJ = dOJ;
	}

	public String getNameOfFather() {
		return nameOfFather;
	}

	public void setNameOfFather(String nameOfFather) {
		this.nameOfFather = nameOfFather;
	}

	public String getNameOfMother() {
		return nameOfMother;
	}

	public void setNameOfMother(String nameOfMother) {
		this.nameOfMother = nameOfMother;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getNameOfSpouse() {
		return nameOfSpouse;
	}

	public void setNameOfSpouse(String nameOfSpouse) {
		this.nameOfSpouse = nameOfSpouse;
	}

	public String getNationalityID() {
		return nationalityID;
	}

	public void setNationalityID(String nationalityID) {
		this.nationalityID = nationalityID;
	}

	public String getPANnumber() {
		return PANnumber;
	}

	public void setPANnumber(String pANnumber) {
		PANnumber = pANnumber;
	}

	public float getExperience() {
		return experience;
	}

	public void setExperience(float experience) {
		this.experience = experience;
	}

	public ContactDetails getContact() {
		return contact;
	}

	public void setContact(ContactDetails contact) {
		this.contact = contact;
	}

	public Set<Address> getAddressSet() {
		return addressSet;
	}

	public void setAddressSet(Set<Address> addressSet) {
		this.addressSet = addressSet;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}
}