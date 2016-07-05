package com.school.employee.ws;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.activation.DataHandler;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import com.school.employee.bean.DocumentRecords;
import com.school.employee.bean.Employee;

@WebService
@SOAPBinding(style = Style.RPC)
public interface EmployeeWS {

	public Employee getEmployee(String employeeID);

	@WebMethod(operationName = "addEmployee")
	@WebResult(partName = "ResultCode")
	public int saveEmployee(@WebParam(partName = "employeeDetailsObject") Employee employee);

	@WebMethod(operationName = "updateEmployee")
	@WebResult(partName = "ResultCode")
	public int updateEmployee(@WebParam(partName = "employeeDetailsObject") Employee employee);

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