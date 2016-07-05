package com.school.employee.test;

import java.io.File;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import com.school.employee.bean.Address;
import com.school.employee.bean.ContactDetails;
import com.school.employee.bean.Employee;
import com.school.employee.ws.EmployeeWSImpl;
import com.school.util.DocumentProp;

public class TestApp {

	public static void main(String[] args) throws Exception {
		EmployeeWSImpl empWs = new EmployeeWSImpl();
		// System.out.println(empWs.getEmployee("1234").getFirstName());

		// addEmp(empWs);
		String fileName = "Aadhaar.pdf";
		File file = new File("C:/Users/Home/Desktop/" + fileName);
		FileDataSource fds = new FileDataSource(file);
		DataHandler dh = new DataHandler(fds);
		String ownerID = "1234";
		int ownerType = DocumentProp.OWNER_EMP;
		int fileType = DocumentProp.AADHAR;
		System.out.println(empWs.saveFile(dh, ownerID, ownerType, fileType, fileName));

		/*
		 * dh = empWs.getFile("1234", DocumentProp.AADHAR); File f = new
		 * File("F:/temp/" + dh.getName());
		 * System.out.println(f.createNewFile()); dh.writeTo(new
		 * FileOutputStream(f)); System.out.println("Downloaded");
		 */

		// System.out.println(empWs.createCredentials("1234",
		// "@scorpio123@".toCharArray()));

		// for (int i = 0; i < 5; i++)
		// System.out.println(empWs.isAuthorized("1234",
		// "@scorpio123@".toCharArray()));
		// System.out.println(empWs.updatePassword("1234",
		// "@scorpio123@".toCharArray(), "@scorpio54321@".toCharArray()));
	}

	public static void addEmp(EmployeeWSImpl empWs) {

		ContactDetails contact = new ContactDetails();
		contact.setMobileNumber1("9811553413");
		contact.setMobileNumber2("9811569449");
		contact.setEmail("aakash.gupta140@outlook.com");
		contact.setWhatsAppNumber("981155343");

		Address adrs = new Address();
		adrs.setAddressType("Present");
		adrs.setAddressLine("58-A Preet Vihar");
		adrs.setState("New Delhi");
		adrs.setCity("Delhi");
		adrs.setPincode("110092");

		Address adrs1 = new Address();
		adrs1.setAddressType("Permanent");
		adrs1.setAddressLine("58-A Krishna Nagar");
		adrs1.setState("UP");
		adrs1.setCity("GZB");
		adrs1.setPincode("110051");

		Set<Address> adrsSet = new HashSet<Address>();
		adrsSet.add(adrs);
		adrsSet.add(adrs1);

		Employee emp = new Employee();
		emp.setEmpID("1234");
		emp.setFirstName("Aakash");
		emp.setLastName("Gupta");
		emp.setGender("Male");
		emp.setBloodGroup("AB+");

		Calendar dob = Calendar.getInstance();
		dob.set(1991, 10, 20);

		emp.setDOB(dob);
		Calendar doj = Calendar.getInstance();
		doj.set(2016, 5, 11);

		emp.setDOJ(doj);

		emp.setDesignation("Principal");
		emp.setMaritalStatus("Single");
		emp.setExperience(2.5F);
		emp.setPANnumber("BCGPG6852F");
		emp.setAddressSet(adrsSet);
		emp.setContact(contact);
		emp.setEmployeeType("Permanent");

		empWs.saveEmployee(emp);

	}

}
