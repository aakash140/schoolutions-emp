package com.school.employee.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;

public interface EmployeeDAO {

	public void save(Object detailsObject);

	public int update(String query);

	public void saveOrUpdate(Object entity);

	public void updateEntity(Object entity);

	public <T extends Object> T get(Class<T> classObj, Serializable id);

	public Object getQueryResult(String query, Map<String, Object> valueMap);

	public List<Object> getQueryResults(String query, Map<String, Object> map);

	public List<Object> getQueryResults(String query);

	public <T extends Object> Criteria getCriteria(Class<T> entity, String alias);

	public List<Object> getSearchResult(Criteria criteria);
}