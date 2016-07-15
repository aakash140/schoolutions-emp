package com.school.employee.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;

public interface EmployeeDAO {

	public void save(Object detailsObject);

	public int update(String query);

	public void updateEntity(Object detailsObject);

	public <T extends Object> T get(Class<T> classObj, Serializable id);

	public Object getQueryResult(String query);

	public List<Object> getQueryResults(String query);

	public <T extends Object> Criteria getCriteria(Class<T> entity, String alias);

	public List<Object> getSearchResult(Criteria criteria);
}