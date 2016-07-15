package com.school.employee.dao;

import java.io.Serializable;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.school.util.StatusCode;

@WebListener
public class EmployeeDAOImpl implements EmployeeDAO, ServletContextListener {

	private Logger logger = Logger.getLogger(EmployeeDAOImpl.class);
	private SessionFactory sessionFactory;
	public Session globalSession;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		setGlobalSession();
	}

	private void setGlobalSession() {
		globalSession = sessionFactory.openSession();
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

	@SuppressWarnings("unchecked")
	@Override
	public int update(String query) {
		logger.info("Updating database record with SQL query: " + query);
		Session session = sessionFactory.openSession();
		Transaction trn = session.beginTransaction();
		Query<Object> queryObj = session.createQuery(query);
		// queryObj.setCacheable(true);
		int result = queryObj.executeUpdate();
		trn.commit();
		return result;
	}

	@Override
	public void updateEntity(Object detailsObject) {
		logger.info("Updating detailsObject: " + detailsObject);
		// Session session = sessionFactory.openSession();
		Transaction trn = globalSession.beginTransaction();
		globalSession.update(detailsObject);
		trn.commit();
		// session.close();
	}

	@Override
	public <T extends Object> T get(Class<T> classObj, Serializable id) {
		logger.info("Fetching " + classObj.toGenericString() + " details with identifier id '" + id + "'");
		// Session session = sessionFactory.openSession();
		T resultEntity = globalSession.get(classObj, id);
		// session.close();
		return resultEntity;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getQueryResult(String query) {
		logger.info("Querying database with SQL Query: " + query);
		Session session = sessionFactory.openSession();
		Query<Object> queryObj = session.createQuery(query);
		// queryObj.setCacheable(true);
		List<Object> resultList = queryObj.getResultList();
		if (resultList != null && resultList.size() > 0) {
			for (Object obj : resultList)
				return obj;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getQueryResults(String query) {
		logger.info("Querying database with SQL Query: " + query);
		Session session = sessionFactory.openSession();
		Query<Object> queryObj = session.createQuery(query);
		// queryObj.setCacheable(true);
		List<Object> resultList = queryObj.getResultList();
		return resultList;
	}

	@SuppressWarnings("deprecation")
	@Override
	public <T extends Object> Criteria getCriteria(Class<T> entity, String alias) {
		Criteria criteria = globalSession.createCriteria(entity, alias);
		return criteria;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getSearchResult(Criteria criteria) {
		return criteria.list();
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		logger.info("Releasing resources...");
		try {
			if (globalSession != null && globalSession.isOpen()) {
				globalSession.close();

			}

			if (sessionFactory != null && sessionFactory.isOpen())
				sessionFactory.close();
		} catch (Exception exc) {
			logger.error("STATUS CODE: " + StatusCode.INTERNAL_ERROR + ": Exception occured while releasing resources\n"
					+ exc);
		}
		/*
		 * ClassLoader cl = Thread.currentThread().getContextClassLoader();
		 *
		 * Set<Thread> threadSet = Thread.getAllStackTraces().keySet(); Thread[]
		 * threadArray = threadSet.toArray(new Thread[threadSet.size()]);
		 *
		 * for (Thread t : threadArray) { if (t.getContextClassLoader() == cl) {
		 * t.interrupt(); } }
		 */
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
	}

}
