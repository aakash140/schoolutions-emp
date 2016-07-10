package com.school.employee.bean;

import java.io.Serializable;
import java.util.Date;

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
	private Date version;

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

		DocumentRecords doc = (DocumentRecords) obj;

		if (this.ownerID.equalsIgnoreCase(doc.getOwnerID()) && (this.getDocType() == doc.getDocType())
				&& (this.getDocLocation().equalsIgnoreCase(doc.getDocLocation()))) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hash = 7 + docType;
		hash = 31 * hash + (null == ownerID ? 0 : ownerID.hashCode());
		hash = 31 * hash + (null == docLocation ? 0 : docLocation.hashCode());
		return hash;
	}
}