package com.school.employee.ws;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;

import javax.activation.DataHandler;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.school.employee.bean.DocumentRecords;
import com.school.employee.bean.Employee;
import com.school.employee.bean.LoginCredentials;
import com.school.employee.dao.EmployeeDAO;
import com.school.util.FileIOUtil;
import com.school.util.PasswordUtil;
import com.school.util.StatusCode;

@SuppressWarnings("deprecation")
@WebService(endpointInterface = "com.school.employee.ws.EmployeeWS")
public class EmployeeWSImpl implements EmployeeWS {

	private Logger logger = Logger.getLogger("EmployeeWSImpl.class");
	private BeanFactory factory;
	private EmployeeDAO dao;
	private Resource resource;

	public EmployeeWSImpl() {
		resource = new ClassPathResource("applicationContext.xml");
		factory = new XmlBeanFactory(resource);
		dao = (EmployeeDAO) factory.getBean("employeeDAO");
	}

	@Override
	public Employee getEmployee(String employeeID) {
		logger.info("Getting details for Employee ID: " + employeeID);
		String query = "FROM Employee EMP WHERE EMP.empID='" + employeeID + "'";
		try {
			Object obj = dao.queryDatabase(query);
			if (obj == null) {
				logger.info("Employee with Employee ID: " + employeeID + " does not exist in records.");
			}
			return (Employee) obj;
		} catch (Exception exception) {
			logger.error("STATUS CODE: " + StatusCode.DBEERROR
					+ ":Exception occured while fetching details for Employee ID: " + employeeID + ":" + exception);
			return null;
		}
	}

	@Override
	public int saveEmployee(Employee employee) {
		logger.info("Saving employee details in database.");
		try {
			String employeeID = employee.getEmpID();
			if (isEmployee(employeeID)) {
				logger.error("STATUS CODE: " + StatusCode.DUPLICATE
						+ ": Cannot persist duplicate employee in database.Employee ID " + employeeID
						+ " already exists in records");
				return StatusCode.DUPLICATE;
			}
			dao.save(employee);
			logger.info("STATUS CODE: " + StatusCode.CREATED + ": Employee details for Employee ID: " + employeeID
					+ " has been successfully saved in database");
			return StatusCode.CREATED;

		} catch (Exception exception) {
			logger.error("STATUS CODE: " + StatusCode.DBEERROR + ": " + exception);
			return StatusCode.DBEERROR;
		}
	}

	@Override
	public int updateEmployee(@WebParam(partName = "employeeDetailsObject") Employee employee) {
		try {
			String employeeID = employee.getEmpID();
			dao.update(employee);
			logger.info("STATUS CODE: " + StatusCode.OK + ": Employee details for Employee ID " + employeeID
					+ " have been updated successfully.");
			return StatusCode.OK;
		} catch (Exception exception) {
			logger.error("STATUS CODE:" + StatusCode.DBEERROR + ": " + exception);
			return StatusCode.DBEERROR;
		}
	}

	@Override
	public int isAuthorized(String userID, char[] password) {
		logger.info("Verifying credentials for User ID:" + userID);
		if (isEmployee(userID)) {
			String query = "FROM LoginCredentials LC WHERE LC.userID='" + userID + "'";
			Object obj = dao.queryDatabase(query);
			if (obj != null) {
				LoginCredentials credentials = (LoginCredentials) obj;
				try {
					if (credentials.getFailedAttempts() != 5
							&& PasswordUtil.validatePassword(credentials.getPassword(), password)) {
						credentials.setFailedAttempts(0);
						updateLoginTimestamp(credentials);
						dao.update(credentials);
						logger.info("STATUS CODE: " + StatusCode.OK + ": Employee ID " + userID + " is authorized");
						return StatusCode.OK;
					} else if (credentials.getFailedAttempts() == 5) {
						logger.error("STATUS CODE: " + StatusCode.LOCKED_OUT + ": Employee ID " + userID
								+ " has been locked out due to 5 consequent incorrect login attempts");
						return StatusCode.LOCKED_OUT;
					} else {
						updateInvalidLoginAttempt(credentials);
						logger.error("STATUS CODE: " + StatusCode.FORBIDDEN
								+ ": Invalid Credentials used while signing in with user id " + userID);
						return StatusCode.FORBIDDEN;
					}
				} catch (InvalidKeySpecException | NoSuchAlgorithmException exception) {
					logger.error("STATUS CODE: " + StatusCode.INTERNAL_ERROR + ":" + exception);
					return StatusCode.INTERNAL_ERROR;
				}
			} else {
				logger.error("STATUS CODE:" + StatusCode.NOT_SIGNED_UP + ": Employee ID " + userID
						+ " has not signed up yet.");
				return StatusCode.NOT_SIGNED_UP;
			}
		} else {
			logger.error(
					"STATUS CODE: " + StatusCode.NOT_FOUND + "Employee ID " + userID + " does not exist in database.");
			return StatusCode.NOT_FOUND;
		}
	}

	@Override
	public int createCredentials(String userID, char[] password) {
		// If he/she is an employee of this school and has not signedUp yet.
		if (isEmployee(userID)) {
			if (!isAlreadySignedUp(userID)) {
				LoginCredentials credentials = (LoginCredentials) factory.getBean("credentialsPOJO");
				credentials.setUserID(userID);
				try {
					credentials.setPassword(PasswordUtil.encryptPassword(password));
					credentials.setFailedAttempts(0);
					dao.save(credentials);
					logger.info("STATUS CODE: " + StatusCode.CREATED + ": Credentials for Employee ID " + userID
							+ " have been successfully created");
					return StatusCode.CREATED;
				} catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
					logger.error("STATUS CODE: " + StatusCode.INTERNAL_ERROR + ": " + exception);
					return StatusCode.INTERNAL_ERROR;
				}
			} else {
				logger.error("STATUS CODE: " + StatusCode.DUPLICATE
						+ ": Cannot persist duplicate credentials in database.User ID " + userID
						+ " already exists in records");
				return StatusCode.DUPLICATE;
			}
		} else {
			logger.error(
					"STATUS CODE: " + StatusCode.NOT_FOUND + "Employee ID " + userID + " does not exist in database.");
			return StatusCode.NOT_FOUND;
		}
	}

	@Override
	public int updatePassword(String userID, char[] oldPassword, char[] newPassword) {
		String query = "FROM LoginCredentials LC WHERE LC.userID='" + userID + "'";
		Object obj = dao.queryDatabase(query);
		LoginCredentials credentials = (LoginCredentials) obj;
		try {
			if (credentials.getFailedAttempts() != 5
					&& PasswordUtil.validatePassword(credentials.getPassword(), oldPassword)) {
				credentials.setPassword(PasswordUtil.encryptPassword(newPassword));
				dao.update(credentials);
				return StatusCode.OK;
			} else if (credentials.getFailedAttempts() == 5) {
				logger.error("STATUS CODE: " + StatusCode.LOCKED_OUT + ": Employee ID " + userID
						+ " has been locked out due to 5 consequent incorrect login attempts");
				return StatusCode.LOCKED_OUT;
			} else {
				logger.error("STATUS CODE: " + StatusCode.FORBIDDEN
						+ ": Invalid Credentials used while updating password for user id " + userID);
				return StatusCode.FORBIDDEN;
			}
		} catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
			logger.error("STATUS CODE: " + StatusCode.INTERNAL_ERROR + ":" + exception);
			return StatusCode.INTERNAL_ERROR;
		}
	}

	@Override
	public DataHandler getFile(String ownerID, int fileType) {
		String query = "SELECT DR.docLocation FROM DocumentRecords DR WHERE DR.docType='" + fileType + "'";
		Object result = dao.queryDatabase(query);
		DataHandler dh = null;
		if (result != null) {
			String fileLoc = (String) result;
			FileIOUtil fileUtil = new FileIOUtil();
			dh = fileUtil.getFile(fileLoc);
		}
		return dh;
	}

	@Override
	public int saveFile(DataHandler dh, String ownerID, int ownerType, int fileType, String fileName) {
		FileIOUtil fileUtil = new FileIOUtil();
		File savedFile = fileUtil.saveFile(dh, ownerID, ownerType, fileType, fileName);
		if (savedFile != null && savedFile.exists()) {
			try {
				DocumentRecords dr = (DocumentRecords) factory.getBean("docRecordsPOJO");
				dr.setOwnerID(ownerID);
				dr.setDocType(fileType);
				dr.setDocLocation(savedFile.toString());
				dao.save(dr);
				return StatusCode.CREATED;
			} catch (Exception exception) {
				logger.error("STATUS CODE: " + StatusCode.DBEERROR + " :Error occured while saving doc/file '"
						+ fileName + "' for ownerID '" + ownerID + "' records in database\n" + exception);
				return StatusCode.DBEERROR;
			}
		} else
			return StatusCode.INTERNAL_ERROR;
	}

	@Override
	public int updateFile(DataHandler dh, String ownerID, int ownerType, int fileType, String fileName) {
		FileIOUtil fileUtil = new FileIOUtil();
		File savedFile = fileUtil.saveFile(dh, ownerID, ownerType, fileType, fileName);
		if (savedFile != null && savedFile.exists()) {
			try {
				DocumentRecords dr = (DocumentRecords) factory.getBean("docRecordsPOJO");
				dr.setOwnerID(ownerID);
				dr.setDocType(fileType);
				dr.setDocLocation(savedFile.toString());
				dao.update(dr);
				return StatusCode.CREATED;
			} catch (Exception exception) {
				logger.error("STATUS CODE: " + StatusCode.DBEERROR + " :Error occured while updating doc/file '"
						+ fileName + "' records in database\n" + exception);
				return StatusCode.DBEERROR;
			}
		} else
			return StatusCode.INTERNAL_ERROR;

	}

	private boolean isEmployee(String userID) {
		String query = "SELECT EMP.empID FROM Employee EMP WHERE EMP.empID='" + userID + "'";
		Object obj = dao.queryDatabase(query);
		return obj != null;
	}

	private boolean isAlreadySignedUp(String userID) {
		String query = "SELECT LC.userID FROM LoginCredentials LC WHERE LC.userID='" + userID + "'";
		Object obj = dao.queryDatabase(query);
		return obj != null;
	}

	private void updateInvalidLoginAttempt(LoginCredentials credentials) {
		int currentFailedAttempts = credentials.getFailedAttempts();
		credentials.setFailedAttempts(++currentFailedAttempts);
		try {
			dao.update(credentials);
			logger.info("Updated invalid login attempts counter for user id " + credentials.getUserID());
		} catch (Exception exception) {
			logger.error("STATUS CODE:" + StatusCode.DBEERROR + ": " + exception);
		}
	}

	private void updateLoginTimestamp(LoginCredentials credentials) {
		LocalDateTime now = LocalDateTime.now();
		credentials.setLastLoginTimestamp(now);
	}
}