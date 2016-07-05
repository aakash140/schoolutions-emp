package com.school.employee.dao;

import java.io.Serializable;
import java.util.List;

public interface EmployeeDAO {

	public void save(Object detailsObject);

	public void update(Object detailsObject);

	public <T extends Object> T get(Class<T> classObj, Serializable id);

	public Object getQueryResult(String query);

	public List<Object> getQueryResults(String query);
}