package com.school.util;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DocListUtil {

	private DocRecord firstRecord;
	private DocRecord currentRecord;

	public void add(int docType, String docName) {
		DocRecord newRecord = new DocRecord();
		newRecord.setDocType(docType);
		newRecord.setDocName(docName);

		if (firstRecord == null) {
			firstRecord = newRecord;
			currentRecord = firstRecord;
		}

		else {
			DocRecord current = firstRecord;
			while (current.next != null)
				current = current.next;
			current.next = newRecord;
		}

	}

	public DocRecord getNext() {
		if (size() > 0) {
			DocRecord temp = currentRecord;
			if (currentRecord != null)
				currentRecord = currentRecord.next;
			return temp;
		}
		return null;
	}

	public int size() {
		DocRecord current = firstRecord;
		int size = 0;
		if (current != null) {
			while (current != null) {
				current = current.next;
				++size;
			}
		}
		return size;
	}

	public boolean hasNext() {
		return currentRecord != null;
	}
}
