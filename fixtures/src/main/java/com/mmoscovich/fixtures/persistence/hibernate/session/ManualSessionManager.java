package com.mmoscovich.fixtures.persistence.hibernate.session;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualSessionManager implements HibernateSessionManager {
	private static final Logger log = LoggerFactory.getLogger(ManualSessionManager.class);
	
	private Session session;

	public ManualSessionManager() {
		log.debug("Using a manually managed session and tx");
	}
	
	@Override
	public void finish() {
		if(isSessionOpen()) {
			try {
				session.getTransaction().commit();
			} finally {
				session.close();
			}
		}
	}

	@Override
	public void rollback() {
		try {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public Session start(SessionFactory sf) {
		try {
			// Open a new session and tx
			session = sf.openSession();
			session.getTransaction().begin();
		} catch(Exception e) {
			if(isSessionOpen()) {
				session.close();
			}
		}
		return session;
	}

	@Override
	public boolean isSessionOpen() {
		return (this.session != null && this.session.isOpen());
	}

}
