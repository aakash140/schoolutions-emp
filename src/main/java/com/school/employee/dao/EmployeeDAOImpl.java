package com.school.employee.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@SuppressWarnings("deprecation")
public class EmployeeDAOImpl implements EmployeeDAO {

	private Logger logger = Logger.getLogger(EmployeeDAOImpl.class);
	public SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void save(Object detailsObject) {
		logger.info("Saving detailsObject: " + detailsObject);
		Session session = sessionFactory.openSession();
		Transaction trn = session.beginTransaction();
		session.persist(detailsObject);
		trn.commit();
		session.close();
	}

	@Override
	public void update(Object detailsObject) {
		logger.info("Updating detailsObject: " + detailsObject);
		Session session = sessionFactory.openSession();
		Transaction trn = session.beginTransaction();
		session.update(detailsObject);
		trn.commit();
		session.close();
	}

	@Override
	public <T extends Object> T get(Class<T> classObj, Serializable id) {
		logger.info("Fetching " + classObj.toGenericString() + " details with identifier id '" + id + "'");
		Session session = sessionFactory.openSession();
		return session.get(classObj, id);

	}

	@Override
	public Object getQueryResult(String query) {
		logger.info("Querying database with SQL Query: " + query);
		Session session = sessionFactory.openSession();
		Query<Object> queryObj = session.createQuery(query);
		Object obj = queryObj.getSingleResult();
		return obj;
	}

	@Override
	public List<Object> getQueryResults(String query) {
		logger.info("Querying database with SQL Query: " + query);
		Session session = sessionFactory.openSession();
		Query<Object> queryObj = session.createQuery(query);
		List<Object> resultList = queryObj.list();
		return resultList;
	}
}
