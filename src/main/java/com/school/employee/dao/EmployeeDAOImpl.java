package com.school.employee.dao;

import java.io.Serializable;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@SuppressWarnings("deprecation")
@WebListener
public class EmployeeDAOImpl implements EmployeeDAO, ServletContextListener {

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

	@SuppressWarnings("unchecked")
	@Override
	public Object getQueryResult(String query) {
		logger.info("Querying database with SQL Query: " + query);
		Session session = sessionFactory.openSession();
		Query<Object> queryObj = session.createQuery(query);
		Object obj = queryObj.getSingleResult();
		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getQueryResults(String query) {
		logger.info("Querying database with SQL Query: " + query);
		Session session = sessionFactory.openSession();
		Query<Object> queryObj = session.createQuery(query);
		List<Object> resultList = queryObj.list();
		return resultList;
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		logger.info("Releasing resources...");
		if (sessionFactory != null && sessionFactory.isOpen())
			sessionFactory.close();

/*		ClassLoader cl = Thread.currentThread().getContextClassLoader();

		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);

		for (Thread t : threadArray) {
			if (t.getContextClassLoader() == cl) {
				t.interrupt();
			}
		}*/
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
	}

}
