package com.school.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.log4j.Logger;

public class FileIOUtil {

	private static Properties prop = new Properties();
	private static Logger logger = Logger.getLogger(FileIOUtil.class);
	private final String ROOT_DIR = prop.getProperty("ROOT_DIR", "C:/TEMP/");
	private final String EMP_DIR = prop.getProperty("EMP_DIR", "Employee/");
	private final String STUD_DIR = prop.getProperty("STUD_DIR", "Student/");
	private final String AADHAR = prop.getProperty("AADHAR", "AADHAR/");
	private final String ACADEMIC_DOC = prop.getProperty("ACADEMIC_DOC", "ACADEMIC_DOC/");
	private final String BIRTH_CERTIFICATE = prop.getProperty("BIRTH_CERTIFICATE", "BIRTH_CERTIFICATE/");
	private final String CHARACTER = prop.getProperty("CHARACTER", "CHARACTER/");
	private final String DP = prop.getProperty("DP", "DP/");
	private final String MEDICAL = prop.getProperty("MEDICAL", "MEDICAL/");
	private final String PAN_CARD = prop.getProperty("PAN_CARD", "PAN_CARD/");
	private final String PASSPORT = prop.getProperty("PASSPORT", "PASSPORT/");
	private final String UNKNOWN = prop.getProperty("UNKNOWN", "UNKNOWN/");
	private final String VOTER_ID = prop.getProperty("VOTER_ID", "VOTER_ID/");

	static {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try (InputStream input = classLoader.getResourceAsStream("docProp.properties");) {
			prop.load(input);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public DataHandler getFile(String fileLoc) {
		File file = new File(fileLoc);
		DataHandler dataHandler = null;
		if (file.exists()) {
			logger.info("Fetching " + fileLoc + " from storage device.");
			FileDataSource fds = new FileDataSource(file);
			dataHandler = new DataHandler(fds);
		} else {
			logger.error(
					"STATUS CODE: " + StatusCode.NOT_FOUND + " :Requested file/doc \"" + fileLoc + "\" was not found");
		}
		return dataHandler;
	}

	public File saveFile(DataHandler dh, String ownerID, int ownerType, int fileType, String fileName) {
		String fileDest = "";
		fileDest = configureDestination(fileType, fileName, ownerID, ownerType);
		try {
			File file = new File(fileDest);
			file.createNewFile();
			FileOutputStream fout = new FileOutputStream(fileDest);
			logger.info("Uploading " + fileName + " for PersonID: " + ownerID);
			dh.writeTo(fout);
			fout.close();
			return file;
		} catch (IOException exception) {
			logger.error("STATUS CODE:" + StatusCode.INTERNAL_ERROR + " :Error occured while uploading a document. "
					+ exception);
			return null;
		}

	}

	private String configureDestination(int fileType, String fileName, String ownerID, int ownerType) {
		String fileDest = ROOT_DIR;
		if (ownerType == DocumentProp.OWNER_EMP)
			fileDest += EMP_DIR;
		else if (ownerType == DocumentProp.OWNER_STUD)
			fileDest += STUD_DIR;

		switch (fileType) {
		case DocumentProp.AADHAR:
			fileDest += AADHAR;
			break;
		case DocumentProp.ACADEMIC_DOC:
			fileDest += ACADEMIC_DOC;
			break;
		case DocumentProp.BIRTH_CERTI:
			fileDest += BIRTH_CERTIFICATE;
			break;
		case DocumentProp.CHAR_DOC:
			fileDest += CHARACTER;
			break;
		case DocumentProp.DP:
			fileDest += DP;
			break;
		case DocumentProp.MEDICAL_DOC:
			fileDest += MEDICAL;
			break;
		case DocumentProp.PAN:
			fileDest += PAN_CARD;
			break;
		case DocumentProp.PASSPORT:
			fileDest += PASSPORT;
			break;
		case DocumentProp.VOTER_ID:
			fileDest += VOTER_ID;
			break;

		default:
			fileDest += UNKNOWN;
			break;
		}

		fileDest += ownerID + File.separator + fileName;
		return fileDest;
	}

	public static void main(String... args) {
		System.out.println("AAKASH");
	}
}
