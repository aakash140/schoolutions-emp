package com.school.employee.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

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
	public Object queryDatabase(String query) {
		logger.info("Querying database with SQL Query: " + query);
		Session session = sessionFactory.openSession();
		Query queryObj = session.createQuery(query);
		List<Object> list = queryObj.list();
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

}
