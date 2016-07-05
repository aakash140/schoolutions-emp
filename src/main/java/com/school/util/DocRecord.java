package com.school.util;

public class DocRecord {

	private int docType;
	private String docName;
	DocRecord next;

	public int getDocType() {
		return docType;
	}

	public void setDocType(int docType) {
		this.docType = docType;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}
}
