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

		boolean isOwnerIDequal = getOwnerID() == doc.getOwnerID()
				|| (getOwnerID() != null && getOwnerID().equalsIgnoreCase(doc.getOwnerID()));

		boolean isDocTypeEqual = getDocType() == doc.getDocType();

		boolean isDocLocEqual = getDocLocation() == doc.getDocLocation()
				|| (getDocLocation() != null && getDocLocation().equalsIgnoreCase(doc.getDocLocation()));

		if (isOwnerIDequal && isDocTypeEqual && isDocLocEqual)
			return true;

		return false;
	}

	@Override
	public int hashCode() {
		int hash = 7 + getDocType();
		hash = 31 * hash + (null == getOwnerID() ? 0 : getOwnerID().hashCode());
		hash = 31 * hash + (null == getDocLocation() ? 0 : getDocLocation().hashCode());
		return hash;
	}
}