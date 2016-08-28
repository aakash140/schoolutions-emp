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

	public DocumentRecords() {
	}

	public DocumentRecords(String ownerID, int docType) {
		this.ownerID = ownerID;
		this.docType = docType;
	}

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

		boolean isOwnerIDequal = ownerID == doc.getOwnerID()
				|| (ownerID != null && ownerID.equalsIgnoreCase(doc.getOwnerID()));

		boolean isDocTypeEqual = docType == doc.getDocType();

		boolean isDocLocEqual = docLocation == doc.getDocLocation()
				|| (docLocation != null && docLocation.equalsIgnoreCase(doc.getDocLocation()));

		if (isOwnerIDequal && isDocTypeEqual && isDocLocEqual)
			return true;

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