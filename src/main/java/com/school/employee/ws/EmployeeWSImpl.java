package com.school.employee.ws;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.school.employee.bean.DocumentRecords;
import com.school.employee.bean.Employee;
import com.school.employee.bean.EmployeeAttendance;
import com.school.employee.bean.LoginCredentials;
import com.school.employee.bean.StartOfDay;
import com.school.employee.bean.UserOTP;
import com.school.employee.dao.EmployeeDAO;
import com.school.util.EmailUtil;
import com.school.util.FileIOUtil;
import com.school.util.PasswordUtil;
import com.school.util.StatusCode;
import com.school.util.search.EmployeeSearchCriteria;

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
		Employee emp = null;
		try {
			emp = dao.getAndCache(Employee.class, employeeID);

			if (emp == null)
				emp = (Employee) factory.getBean("employeePOJO");

			return emp;
		} catch (Exception exception) {
			logger.error(
					"STATUS CODE: " + StatusCode.DBEERROR + ":Exception occured while fetching details for employee '"
							+ employeeID + "' \n" + getExceptionDetail(exception));
			return null;
		}
	}

	@Override
	public int saveEmployee(Employee employee) {
		try {
			String employeeID = employee.getEmpID();

			if (isEmployee(employeeID)) {
				logger.error("STATUS CODE: " + StatusCode.DUPLICATE
						+ ": Duplicate details cannot be saved for employee '" + employeeID + "'.");
				return StatusCode.DUPLICATE;
			}

			dao.save(employee);
			logger.info("STATUS CODE: " + StatusCode.CREATED + ": Employee details for employee '" + employeeID
					+ "' have been successfully saved in database");

			return StatusCode.CREATED;

		} catch (Exception exception) {
			logger.error("STATUS CODE: " + StatusCode.DBEERROR + ": Exception occured while saving the employee '"
					+ employee.getEmpID() + "'\n" + getExceptionDetail(exception));

			return StatusCode.DBEERROR;
		}
	}

	@Override
	public int deactivateEmployee(String loginID, String employeeID) {
		try {
			Employee emp = dao.get(Employee.class, employeeID);
			if (emp != null) {
				emp.setStatus("0");
				dao.updateEntity(emp);

				logger.info("STATUS CODE: " + StatusCode.OK + ": Employee '" + employeeID
						+ "' has been deactivated by employee '" + loginID + "'");

				return StatusCode.OK;
			} else
				return StatusCode.NOT_FOUND;
		} catch (Exception exception) {
			logger.error("STATUS CODE: " + StatusCode.DBEERROR + ": Exception occured while deactivating the employee '"
					+ employeeID + "'\n" + getExceptionDetail(exception));
			return StatusCode.DBEERROR;
		}
	}

	@Override
	public int activateEmployee(String loginID, String employeeID) {
		try {
			Employee emp = dao.get(Employee.class, employeeID);
			if (emp != null) {
				emp.setStatus("1");
				dao.updateEntity(emp);

				logger.info("STATUS CODE: " + StatusCode.OK + ": Employee '" + employeeID
						+ "' has been activated by employee '" + loginID + "'");

				return StatusCode.OK;
			} else
				return StatusCode.NOT_FOUND;
		} catch (Exception exception) {
			logger.error("STATUS CODE: " + StatusCode.DBEERROR + ": Exception occured while activating the employee '"
					+ employeeID + "'\n" + getExceptionDetail(exception));
			return StatusCode.DBEERROR;
		}
	}

	@Override
	public int unlockEmployee(String loginID, String employeeID) {
		if (isEmployee(employeeID)) {
			if (isAlreadySignedUp(employeeID)) {
				String query = "UPDATE LoginCredentials LC SET LC.failedAttempts=:attempts WHERE LC.userID=:userID";

				Map<String, Object> valueMap = new HashMap<>();
				valueMap.put("attempts", 0);
				valueMap.put("userID", employeeID);

				try {
					dao.update(query, valueMap);

					logger.error("STATUS CODE: " + StatusCode.DBEERROR + ": Employee '" + employeeID
							+ "' has been unlocked by employee '" + loginID + "'");

					return StatusCode.OK;
				} catch (Exception exception) {
					logger.error(
							"STATUS CODE: " + StatusCode.DBEERROR + ": Exception occured while unlocking the employee '"
									+ employeeID + "'\n" + getExceptionDetail(exception));
					return StatusCode.DBEERROR;
				}
			} else
				return StatusCode.NOT_SIGNED_UP;
		} else
			return StatusCode.NOT_FOUND;
	}

	@Override
	public int updateEmployee(Employee employee) {
		String employeeID = employee.getEmpID();
		try {
			dao.updateEntity(employee);

			logger.info("STATUS CODE: " + StatusCode.OK + ": Employee details for employee '" + employeeID
					+ "' have been updated successfully.");

			return StatusCode.OK;
		} catch (Exception exception) {
			logger.error("STATUS CODE:" + StatusCode.DBEERROR
					+ ": Exception occured while updating the employee details for employee '" + employeeID + "' \n"
					+ getExceptionDetail(exception));
			return StatusCode.DBEERROR;
		}
	}

	@Override
	public int isAuthorized(String userID, char[] password) {
		logger.info("Verifying credentials for User ID:" + userID);

		if (isEmployee(userID)) {

			LoginCredentials credentials = dao.getAndCache(LoginCredentials.class, userID);
			if (credentials != null) {
				try {
					if (credentials.getFailedAttempts() < 5
							&& PasswordUtil.isvalidPassword(credentials.getPassword(), password)) {

						credentials.setFailedAttempts(0);
						updateLoginTimestamp(credentials);
						dao.updateEntity(credentials);
						logger.info("STATUS CODE: " + StatusCode.OK + ": Employee ID " + userID + " is authorized");

						return StatusCode.OK;
					}

					else if (credentials.getFailedAttempts() == 5) {
						return StatusCode.LOCKED_OUT;
					}

					else {
						updateInvalidLoginAttempt(credentials);
						return StatusCode.FORBIDDEN;
					}
				}

				catch (InvalidKeySpecException | NoSuchAlgorithmException exception) {
					logger.error("STATUS CODE: " + StatusCode.INTERNAL_ERROR + "\n" + getExceptionDetail(exception));
					return StatusCode.INTERNAL_ERROR;
				}
			}

			else {
				return StatusCode.NOT_SIGNED_UP;
			}
		}

		else {
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
				}

				catch (Exception exception) {
					logger.error("STATUS CODE: " + StatusCode.INTERNAL_ERROR + "\n" + getExceptionDetail(exception));
					return StatusCode.INTERNAL_ERROR;
				}
			}

			else {
				return StatusCode.DUPLICATE;
			}
		}

		else {
			return StatusCode.NOT_FOUND;
		}
	}

	@Override
	public int updatePassword(String userID, char[] oldPassword, char[] newPassword) {
		LoginCredentials credentials = dao.get(LoginCredentials.class, userID);

		try {
			boolean isValidOldPass = PasswordUtil.isvalidPassword(credentials.getPassword(), oldPassword);

			if (credentials != null && credentials.getFailedAttempts() != 5 && isValidOldPass) {
				credentials.setPassword(PasswordUtil.encryptPassword(newPassword));
				dao.updateEntity(credentials);

				return StatusCode.OK;
			} else if (credentials != null && credentials.getFailedAttempts() == 5) {
				logger.info("STATUS CODE: " + StatusCode.LOCKED_OUT
						+ ": Could not update the password for locked out employee '" + userID + "'");

				return StatusCode.LOCKED_OUT;
			} else {

				return StatusCode.FORBIDDEN;
			}
		} catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
			logger.error("STATUS CODE: " + StatusCode.INTERNAL_ERROR + "\n" + getExceptionDetail(exception));
			return StatusCode.INTERNAL_ERROR;
		}
	}

	// This method assumes that an OTP is already available in database for
	// given userID.
	@Override
	public int updatePassword(String userID, String submittedOTP, char[] newPassword) {

		if (isAlreadySignedUp(userID)) {

			UserOTP usrOTP = dao.get(UserOTP.class, userID);
			if (usrOTP != null) {
				LoginCredentials credentials = dao.get(LoginCredentials.class, userID);
				String savedOTP = usrOTP.getOTP();
				LocalDateTime generationTime = usrOTP.getOTPTimestamp();
				LocalDateTime now = LocalDateTime.now();
				long minutesElapsed = ChronoUnit.MINUTES.between(generationTime, now);
				boolean isOTPActive = usrOTP.getActiveStatus() == 1 ? true : false;

				if (savedOTP.equalsIgnoreCase(submittedOTP) && isOTPActive) {
					if (minutesElapsed <= 15) {
						try {
							credentials.setPassword(PasswordUtil.encryptPassword(newPassword));
							dao.updateEntity(credentials);
							deactivateOTP(usrOTP);
							return StatusCode.OK;
						} catch (Exception exception) {
							logger.error("STATUS CODE: " + StatusCode.INTERNAL_ERROR
									+ ": Error occured while updating password for user id '" + userID + "'\n"
									+ getExceptionDetail(exception));

							return StatusCode.INTERNAL_ERROR;
						}
					} else {
						deactivateOTP(usrOTP);
						return StatusCode.FORBIDDEN;
					}
				} else
					return StatusCode.FORBIDDEN;
			} else
				return StatusCode.INTERNAL_ERROR;
		} else
			return StatusCode.NOT_FOUND;
	}

	@Override
	public int beginSchoolSession(Calendar workDay, String employeeID) {
		if (!isSchoolAlreadyOpen(workDay)) {
			StartOfDay sod = (StartOfDay) factory.getBean("sodPOJO");
			LocalDateTime now = LocalDateTime.now();
			sod.setWorkDay(workDay);
			sod.setEmployeeID(employeeID);
			sod.setTimestamp(now);
			try {
				dao.save(sod);
				logger.info("School Session on " + workDay.toString() + " began at " + now.toString());
				return StatusCode.CREATED;
			} catch (Exception exception) {
				logger.error("STATUS CODE:" + StatusCode.DBEERROR + ": An error occured while starting the day on "
						+ workDay.toString() + ".\n" + getExceptionDetail(exception));
				return StatusCode.DBEERROR;
			}
		} else
			return StatusCode.DUPLICATE;
	}

	/**
	 * Before calling this method caller should verify the validity of employee
	 * id(s) and Date of work.
	 *
	 * @param workDay
	 * @param employeeIDs
	 * @param status
	 * @return Status Code
	 */
	@Override
	public int markAttendance(Calendar workDay, String[] employeeIDs, String status) {
		for (String empID : employeeIDs) {
			EmployeeAttendance atndnc = dao.getAndCache(EmployeeAttendance.class,
					new EmployeeAttendance(workDay, empID));
			LocalDateTime now = LocalDateTime.now();
			atndnc.setStatus(status);
			atndnc.setTimestamp(now);
			try {
				dao.updateEntity(atndnc);
				logger.info("Attendance for employee '" + empID + "'for " + workDay.toString() + " has been marked at "
						+ now.toString());
			} catch (Exception exception) {
				logger.error("STATUS CODE:" + StatusCode.DBEERROR
						+ ": An error occured while marking the attendance for employee '" + empID + "'for "
						+ workDay.toString() + "\n" + getExceptionDetail(exception));
				return StatusCode.DBEERROR;
			}
		}
		return StatusCode.OK;
	}

	@Override
	public StartOfDay[] getWorkDays(Calendar startDate, Calendar endDate) {

		Criteria criteria = dao.getCriteria(StartOfDay.class, "SOD");
		criteria.add(Restrictions.between("SOD.workDay", startDate, endDate));
		criteria.addOrder(Order.asc("SOD.workDay"));

		try {
			List<Object> resultList = dao.getSearchResult(criteria);

			if (resultList != null && resultList.size() > 0) {
				StartOfDay[] sod = resultList.toArray(new StartOfDay[resultList.size()]);
				return sod;
			} else
				return new StartOfDay[resultList.size()];
		} catch (Exception exception) {
			logger.error("STATUS CODE: " + StatusCode.INTERNAL_ERROR
					+ "An error occured while searching for workdays.\n" + getExceptionDetail(exception));
			return null;
		}
	}

	@Override
	public EmployeeAttendance[] getEmployeeAttendanceReport(String employeeID, Calendar startDate, Calendar endDate) {
		Criteria criteria = dao.getCriteria(EmployeeAttendance.class, "Emp");

		criteria.add(Restrictions.eq("Emp.employeeID", employeeID).ignoreCase());
		criteria.add(Restrictions.between("Emp.workDay", startDate, endDate));
		criteria.addOrder(Order.asc("Emp.workDay"));
		try {
			List<Object> resultList = dao.getSearchResult(criteria);
			if (resultList != null && resultList.size() > 0) {
				EmployeeAttendance[] empAtndnc = resultList.toArray(new EmployeeAttendance[resultList.size()]);
				return empAtndnc;
			} else
				return new EmployeeAttendance[resultList.size()];
		} catch (Exception exception) {
			logger.error("STATUS CODE: " + StatusCode.INTERNAL_ERROR
					+ "An error occured while searching attendance for employee '" + employeeID + "'.\n"
					+ getExceptionDetail(exception));
			return null;
		}
	}

	@Override
	public EmployeeAttendance[] getAttendanceOnDate(Calendar workday) {
		Criteria criteria = dao.getCriteria(EmployeeAttendance.class, "Emp");

		criteria.add(Restrictions.eq("Emp.workDay", workday));
		criteria.addOrder(Order.asc("Emp.workDay"));
		try {
			List<Object> resultList = dao.getSearchResult(criteria);
			if (resultList != null && resultList.size() > 0) {
				EmployeeAttendance[] empAtndnc = resultList.toArray(new EmployeeAttendance[resultList.size()]);
				return empAtndnc;
			} else
				return new EmployeeAttendance[resultList.size()];
		} catch (Exception exception) {
			logger.error(
					"STATUS CODE: " + StatusCode.INTERNAL_ERROR + "An error occured while searching attendance for '"
							+ workday.toString() + "'.\n" + getExceptionDetail(exception));
			return null;
		}
	}

	@Override
	public Employee[] searchEmployees(EmployeeSearchCriteria searchCrt) {
		String fName = searchCrt.getfirstName();
		String lName = searchCrt.getlastName();
		String contactNum = searchCrt.getContactNum();
		String gender = searchCrt.getGender();
		String employeeType = searchCrt.getEmployeeType();
		Calendar dob = searchCrt.getDob();
		Calendar doj = searchCrt.getDoj();
		String bloodgroup = searchCrt.getBloodgroup();
		int department = searchCrt.getDepartment();
		String designation = searchCrt.getDesignation();
		int maritalStatus = searchCrt.getMaritalStatus();

		Criteria criteria = dao.getCriteria(Employee.class, "EMP");

		if (fName != null && fName.length() > 0)
			criteria.add(Restrictions.ilike("EMP.firstName", fName + "%"));

		if (lName != null && lName.length() > 0)
			criteria.add(Restrictions.ilike("EMP.lastName", lName + "%"));

		if (contactNum != null && contactNum.length() > 0) {
			criteria.createAlias("EMP.contact", "CNTCT");
			Criterion mob1 = Restrictions.eq("CNTCT.mobileNumber1", contactNum);
			Criterion mob2 = Restrictions.eq("CNTCT.mobileNumber2", contactNum);
			Criterion whatsApp = Restrictions.eq("CNTCT.whatsAppNumber", contactNum);
			Disjunction expr = Restrictions.or(mob1, mob2, whatsApp);
			criteria.add(expr);
		}

		if (gender != null && gender.length() > 0)
			criteria.add(Restrictions.eq("EMP.gender", gender).ignoreCase());

		if (employeeType != null && employeeType.length() > 0)
			criteria.add(Restrictions.ilike("EMP.employeeType", "%" + employeeType + "%"));

		if (dob != null)
			criteria.add(Restrictions.ge("EMP.DOB", dob));

		if (doj != null)
			criteria.add(Restrictions.ge("EMP.DOJ", doj));

		if (bloodgroup != null && bloodgroup.length() > 0)
			criteria.add(Restrictions.eq("EMP.bloodGroup", bloodgroup).ignoreCase());

		if (department >= 0)
			criteria.add(Restrictions.eq("EMP.department", department));

		if (designation != null && designation.length() > 0)
			criteria.add(Restrictions.ilike("EMP.designation", "%" + designation + "%"));

		if (maritalStatus >= 0)
			criteria.add(Restrictions.eq("EMP.maritalStatus", maritalStatus));

		criteria.add(Restrictions.eq("EMP.status", "1"));

		criteria.addOrder(Order.asc("EMP.empID"));

		try {
			List<Object> list = dao.getSearchResult(criteria);

			if (list != null && list.size() > 0) {
				Employee[] employees = list.toArray(new Employee[list.size()]);
				return employees;
			} else
				return new Employee[list.size()];
		} catch (Exception exception) {
			logger.error("STATUS CODE: " + StatusCode.DBEERROR + ": An exception occured during employee lookup.\n"
					+ getExceptionDetail(exception));
			return null;
		}

	}

	public long countEmployees(EmployeeSearchCriteria searchCrt) {
		Employee[] emps = searchEmployees(searchCrt);
		if (emps != null)
			return emps.length;
		else
			return 0;
	}

	@Override
	public DataHandler getFile(String ownerID, int fileType) {
		logger.info("Getting file with File Type '" + fileType + "' for owner '" + ownerID + "'");

		DocumentRecords docRec = dao.get(DocumentRecords.class, new DocumentRecords(ownerID, fileType));
		DataHandler dh = null;
		if (docRec != null) {
			String fileLoc = docRec.getDocLocation();
			FileIOUtil fileUtil = new FileIOUtil();
			dh = fileUtil.getFile(fileLoc);
		} else
			logger.info("File with file Type '" + fileType + "' for owner '" + ownerID + "' does not exist.");
		return dh;
	}

	@Override
	public int saveFile(DataHandler dh, String ownerID, int ownerType, int fileType) {
		logger.info("Saving file with File Type '" + fileType + "' for owner '" + ownerID + "'");
		FileIOUtil fileUtil = new FileIOUtil();
		File savedFile = fileUtil.saveFile(dh, ownerID, ownerType, fileType);
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
						+ dh.getName() + "' for ownerID '" + ownerID + "' records in database\n"
						+ getExceptionDetail(exception));
				return StatusCode.DBEERROR;
			}
		} else
			return StatusCode.INTERNAL_ERROR;
	}

	@Override
	public int updateFile(DataHandler dh, String ownerID, int ownerType, int fileType) {
		logger.info("Updating file with File Type '" + fileType + "' for owner '" + ownerID + "'");
		FileIOUtil fileUtil = new FileIOUtil();
		File savedFile = fileUtil.saveFile(dh, ownerID, ownerType, fileType);
		if (savedFile != null && savedFile.exists()) {
			try {
				DocumentRecords dr = (DocumentRecords) factory.getBean("docRecordsPOJO");
				dr.setOwnerID(ownerID);
				dr.setDocType(fileType);
				dr.setDocLocation(savedFile.toString());
				dao.updateEntity(dr);
				return StatusCode.CREATED;
			} catch (Exception exception) {
				logger.error("STATUS CODE: " + StatusCode.DBEERROR + " :Error occured while updating doc/file '"
						+ dh.getName() + "' records in database\n" + getExceptionDetail(exception));
				return StatusCode.DBEERROR;
			}
		} else
			return StatusCode.INTERNAL_ERROR;

	}

	@Override
	public int[] getFileNames(String ownerID) {
		String query = "FROM DocumentRecords DR where DR.ownerID=  :ownerID ";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("ownerID", ownerID);
		List<Object> resultList = dao.getQueryResults(query, paramMap);
		if (resultList != null) {
			int[] docArray = new int[resultList.size()];
			Object objArray[] = resultList.toArray();
			for (int i = 0; i < objArray.length; i++) {
				DocumentRecords docRecrd = (DocumentRecords) objArray[i];
				docArray[i] = docRecrd.getDocType();
			}
			return docArray;
		}
		return null;
	}

	@Override
	public int sendAnEmail(String employeeID, String messageBody, String subject, DataHandler[] attachments) {
		Criteria criteria = dao.getCriteria(Employee.class, "EMP");
		criteria.add(Restrictions.eq("EMP.empID", employeeID));
		criteria.createAlias("EMP.contact", "CNTCT");
		criteria.setProjection(Projections.property("CNTCT.email"));
		try {
			List<Object> result = dao.getSearchResult(criteria);
			if (result != null && result.size() > 0) {
				String recepient = (String) result.get(0);
				EmailUtil emailUtil = new EmailUtil();
				int status = emailUtil.sendEmail(recepient, messageBody, subject, attachments);
				return status;
			} else {
				return StatusCode.EMAIL_NOT_FOUND;
			}

		} catch (Exception exp) {
			exp.printStackTrace();
			return StatusCode.DBEERROR;
		}
	}

	@Override
	public int emailandSaveOTP(String employeeID, String OTP) {
		if (isAlreadySignedUp(employeeID)) {
			Criteria criteria = dao.getCriteria(Employee.class, "EMP");
			criteria.add(Restrictions.eq("EMP.empID", employeeID));
			criteria.createAlias("EMP.contact", "CNTCT");
			criteria.setProjection(Projections.property("CNTCT.email"));
			try {
				List<Object> result = dao.getSearchResult(criteria);
				if (result != null && result.size() > 0) {
					String recepient = (String) result.get(0);
					EmailUtil emailUtil = new EmailUtil();
					int status = emailUtil.emailOTP(recepient, OTP);

					if (status == StatusCode.OK) {
						UserOTP usrOTP = dao.get(UserOTP.class, employeeID);
						if (usrOTP == null)
							usrOTP = (UserOTP) factory.getBean("userOTPPOJO");
						usrOTP.setUserID(employeeID);
						usrOTP.setOTP(OTP);
						usrOTP.setOTPTimestamp(LocalDateTime.now());
						dao.saveOrUpdate(usrOTP);
						return StatusCode.OK;
					} else
						return StatusCode.EMAILERROR;
				} else
					return StatusCode.EMAIL_NOT_FOUND;
			} catch (Exception exception) {
				logger.error(
						"STATUS CODE: " + StatusCode.DBEERROR + ". Exception occured while saving OTP for employee '"
								+ employeeID + "'.\n" + getExceptionDetail(exception));
				return StatusCode.DBEERROR;
			}

		} else
			return StatusCode.NOT_FOUND;
	}

	@Override
	public boolean isEmployee(String userID) {
		String query = "SELECT EMP.empID FROM Employee EMP WHERE EMP.empID=:userID AND EMP.status=:status";
		Map<String, Object> valueMap = new HashMap<>();
		valueMap.put("userID", userID);
		valueMap.put("status", "1");
		Object obj = dao.getQueryResult(query, valueMap);
		return obj != null;
	}

	@Override
	public boolean isSchoolAlreadyOpen(Calendar workDay) {
		StartOfDay sod = dao.getAndCache(StartOfDay.class, workDay);
		return sod != null;
	}

	private boolean isAlreadySignedUp(String userID) {
		String query = "SELECT LC.userID FROM LoginCredentials LC WHERE LC.userID=:userID";
		Map<String, Object> valueMap = new HashMap<>();
		valueMap.put("userID", userID);
		Object obj = dao.getQueryResult(query, valueMap);
		return obj != null;
	}

	private void updateInvalidLoginAttempt(LoginCredentials credentials) {
		int currentFailedAttempts = credentials.getFailedAttempts();
		credentials.setFailedAttempts(++currentFailedAttempts);
		try {
			dao.updateEntity(credentials);
			logger.info("Updated invalid login attempts counter for user id " + credentials.getUserID());
		} catch (Exception exception) {
			logger.error("STATUS CODE:" + StatusCode.DBEERROR + ": " + getExceptionDetail(exception));
		}
	}

	private void updateLoginTimestamp(LoginCredentials credentials) {
		LocalDateTime now = LocalDateTime.now();
		credentials.setLastLoginTimestamp(now);
	}

	private void deactivateOTP(UserOTP usrOTP) {
		usrOTP.setActiveStatus(0);
		try {
			dao.updateEntity(usrOTP);
		} catch (Exception exception) {
			logger.error("STATUS CODE: " + StatusCode.DBEERROR + ": Error occured while deactivating OTP for user ID '"
					+ usrOTP.getUserID() + "'.\n" + getExceptionDetail(exception));
		}
	}

	private String getExceptionDetail(Throwable exception) {
		StringBuffer sb = new StringBuffer();
		while (exception != null) {
			sb.append(exception.toString());
			exception = exception.getCause();
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		// new EmployeeWSImpl().getEmployee("1234");
		// new EmployeeWSImpl().deactivateEmployee("1234");
		File f1 = new File("C:\\Users\\Home\\Desktop\\1.jpg");
		File f2 = new File("C:\\Users\\Home\\Desktop\\CREATE_TABLE.sql");
		File f3 = new File("C:\\Users\\Home\\Desktop\\possuite-141-dm-ddl.rar");

		DataHandler dh1 = new DataHandler(new FileDataSource(f1));
		DataHandler dh2 = new DataHandler(new FileDataSource(f2));
		DataHandler dh3 = new DataHandler(new FileDataSource(f3));

		DataHandler[] dh = new DataHandler[] { dh1, dh2, dh3 };
		// String[] recepientList = new String[] { "aakash.gupta140@gmail.com",
		// "aakash.gupta140@outlook.com",
		// "vikas.gupta0502@outlook.com", "pkgn1965@gmail.com" };
		// new EmployeeWSImpl().sendAnEmail("1234", "Test", "Test", dh);
		String[] empIDs = new String[] { "1234", "4321", "1111" };
		// int result = new EmployeeWSImpl().beginSchoolSession(new Date(2016,
		// 7, 26), "1234");
		// int result = new EmployeeWSImpl().markAttendance(new Date(2016, 9,
		// 26), empIDs, "1");

		Calendar sd = Calendar.getInstance();
		sd.set(2016, 7, 25);

		Calendar ed = Calendar.getInstance();
		ed.set(2016, 7, 27);

		StartOfDay[] result = new EmployeeWSImpl().getWorkDays(ed, ed);
		System.out.println(result);
	}
}