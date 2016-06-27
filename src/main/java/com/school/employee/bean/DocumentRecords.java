package com.school.employee.bean;

import java.io.Serializable;

/**
 * Implemented Serializable as it has composite key.
 *
 * @author Aakash Gupta
 *
 */
public class DocumentRecords implements Serializable {

	private static final long serialVersionUID = 8986728537415326397L;

	private String ownerID;
	private int docType;
	private String docLocation;

	public String getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}

	public int getDocType() {
		return docType;
	}

	public void setDocType(int docType) {
		this.docType = docType;
	}

	public String getDocLocation() {
		return docLocation;
	}

	public void setDocLocation(String docLocation) {
		this.docLocation = docLocation;
	}
}