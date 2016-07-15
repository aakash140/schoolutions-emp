package com.school.employee.ws;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;

import javax.activation.DataHandler;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.school.employee.bean.DocumentRecords;
import com.school.employee.bean.Employee;

@WebService
public interface EmployeeWS {

	public Employee getEmployee(String employeeID);

	@WebMethod(operationName = "addEmployee")
	@WebResult(partName = "ResultCode")
	public int saveEmployee(@WebParam(partName = "employeeDetailsObject") Employee employee);

	@WebMethod(operationName = "updateEmployee")
	@WebResult(partName = "ResultCode")
	public int updateEmployee(@WebParam(partName = "employeeDetailsObject") Employee employee);

	@WebMethod(operationName = "deleteEmployee")
	@WebResult(partName = "ResultCode")
	public int deactivateEmployee(@WebParam(partName = "employeeID") String employeeID);

	@WebMethod
	@WebResult(partName = "ResultCode")
	public int isAuthorized(@WebParam(partName = "userID") String userID,
			@WebParam(partName = "loginPassword") char[] password)
			throws InvalidKeySpecException, NoSuchAlgorithmException;

	@WebMethod(operationName = "signupEmployee")
	@WebResult(partName = "ResultCode")
	public int createCredentials(@WebParam(partName = "userID") String userID,
			@WebParam(partName = "loginPassword") char[] password);

	@WebResult(partName = "ResultCode")
	public int updatePassword(@WebParam(partName = "userID") String userID,
			@WebParam(partName = "oldPassword") char[] oldPassword,
			@WebParam(partName = "newPassword") char[] newPassword);

	@WebMethod(operationName = "lookupEmployee")
	@WebResult(partName = "EmployeeArray")
	public Employee[] searchEmployee(@WebParam(partName = "firstName") String fName,
			@WebParam(partName = "lastName") String lName, @WebParam(partName = "contactNumber") String contactNum,
			@WebParam(partName = "gender") String gender, @WebParam(partName = "employeeType") String employeeType,
			@WebParam(partName = "DOB") Calendar dob, @WebParam(partName = "DOJ") Calendar doj,
			@WebParam(partName = "bloodGroup") String bloodgroup, @WebParam(partName = "departmentID") int department,
			@WebParam(partName = "designation") String designation,
			@WebParam(partName = "maritalStatus") int maritalStatus);

	@WebMethod(operationName = "countEmployees")
	@WebResult(partName = "ResultCount")
	public long countEmployees(@WebParam(partName = "firstName") String fName,
			@WebParam(partName = "lastName") String lName, @WebParam(partName = "gender") String gender,
			@WebParam(partName = "employeeType") String employeeType, @WebParam(partName = "DOB") Calendar dob,
			@WebParam(partName = "DOJ") Calendar doj, @WebParam(partName = "bloodGroup") String bloodgroup,
			@WebParam(partName = "departmentID") int department, @WebParam(partName = "designation") String designation,
			@WebParam(partName = "maritalStatus") int maritalStatus);

	@WebMethod(operationName = "downloadFile")
	@WebResult(partName = "ByteArray")
	public DataHandler getFile(@WebParam(partName = "DocOwnerID") String ownerID,
			@WebParam(partName = "DocType") int fileType);

	@WebMethod(operationName = "uploadFile")
	@WebResult(partName = "UploadStatus")
	public int saveFile(@WebParam(partName = "ByteArray") DataHandler dh,
			@WebParam(partName = "FileOwnerID") String ownerID, @WebParam(partName = "FileOwnerType") int ownerType,
			@WebParam(partName = "FileType") int fileType, @WebParam(partName = "FileName") String fileName);

	@WebMethod(operationName = "updateFile")
	@WebResult(partName = "UploadStatus")
	public int updateFile(@WebParam(partName = "ByteArray") DataHandler dh,
			@WebParam(partName = "FileOwnerID") String ownerID, @WebParam(partName = "FileOwnerType") int ownerType,
			@WebParam(partName = "FileType") int fileType, @WebParam(partName = "FileName") String fileName);

	@WebMethod
	@WebResult(partName = "DocRecordsArray")
	public DocumentRecords[] getFileNames(@WebParam(partName = "DocOwnerID") String ownerID);
}