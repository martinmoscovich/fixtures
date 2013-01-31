package com.mmoscovich.fixtures.persistence.hibernate.session;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientManagedSessionManager implements HibernateSessionManager {
	private static final Logger log = LoggerFactory.getLogger(ClientManagedSessionManager.class);
	
	private Session session;
	
	public ClientManagedSessionManager() {
		log.debug("Using client managed session and tx");
	}

	@Override
	public void finish() {
		// Do nothing
	}

	@Override
	public void rollback() {
		// Do nothing
	}

	@Override
	public Session start(SessionFactory sf) {
		// Get the current session (managed by the client)
		session = sf.getCurrentSession();
		return session;
	}

	@Override
	public boolean isSessionOpen() {
		return (this.session != null && this.session.isOpen());
	}

}
