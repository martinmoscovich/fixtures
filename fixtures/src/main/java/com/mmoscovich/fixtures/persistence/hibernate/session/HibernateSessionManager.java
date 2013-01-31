package com.mmoscovich.fixtures.persistence.hibernate.session;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public interface HibernateSessionManager {
	Session start(SessionFactory sf);
	void finish();
	void rollback();
	boolean isSessionOpen();
}
