package com.school.employee.bean;

import java.util.Date;

public class Address {

	private int addressID;
	private String addressType;
	private String addressLine;
	private String state;
	private String city;
	private String pincode;
	private Date version;

	public int getAddressID() {
		return addressID;
	}

	public void setAddressID(int addressID) {
		this.addressID = addressID;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getAddressLine() {
		return addressLine;
	}

	public void setAddressLine(String addressLine) {
		this.addressLine = addressLine;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null || !(this.getClass() == obj.getClass()))
			return false;

		Address adrs = (Address) obj;
		if (this.getAddressID() == adrs.getAddressID() && this.getAddressLine().equalsIgnoreCase(adrs.getAddressLine())
				&& this.getAddressType().equalsIgnoreCase(adrs.getAddressType())
				&& this.getState().equalsIgnoreCase(adrs.getState()) && this.getCity().equalsIgnoreCase(adrs.getCity())
				&& this.getPincode().equalsIgnoreCase(adrs.getPincode())) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + addressID;
		hash = 31 * hash + (null == addressType ? 0 : addressType.hashCode());
		hash = 31 * hash + (null == addressLine ? 0 : addressLine.hashCode());
		hash = 31 * hash + (null == state ? 0 : state.hashCode());
		hash = 31 * hash + (null == city ? 0 : city.hashCode());
		hash = 31 * hash + (null == pincode ? 0 : pincode.hashCode());
		return hash;
	}

	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}
}
