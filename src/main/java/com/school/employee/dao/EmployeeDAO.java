package com.school.employee.dao;

public interface EmployeeDAO {

	public void save(Object detailsObject);

	public void update(Object detailsObject);

	public Object queryDatabase(String query);
}