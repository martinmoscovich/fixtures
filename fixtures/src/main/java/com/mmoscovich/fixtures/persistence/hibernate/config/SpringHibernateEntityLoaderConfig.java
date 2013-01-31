package com.mmoscovich.fixtures.persistence.hibernate.config;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Implementation of {@link HibernateEntityLoaderConfig} that loads the configured {@link SessionFactory} from a Spring context.
 * 
 * @author Martin.Moscovich
 *
 */
public class SpringHibernateEntityLoaderConfig extends HibernateEntityLoaderConfigBase {
	
	private ApplicationContext context;
	private String beanName = null;
	private boolean springTx;
	
	/**
	 * Retrieves the {@link SessionFactory} from the passed context.
	 * Use this constructor if there's only one {@link SessionFactory} in your Spring's context
	 * @param context Spring context
	 * @param springManagedTransactions indicates whether Spring manages transactions
	 */
	public SpringHibernateEntityLoaderConfig(ApplicationContext context, boolean springManagedTransactions) {
		this(context, null, springManagedTransactions);
	}

	/**
	 * Retrieves the {@link SessionFactory} from the passed context using the specified bean name
	 * Use this constructor if there's more than one {@link SessionFactory} in your Spring's context
	 * @param context Spring context
	 * @param springManagedTransactions indicates whether Spring manages transactions
	 */
	public SpringHibernateEntityLoaderConfig(ApplicationContext context, String sessionFactoryBeanName, boolean springManagedTransactions) {
		this.context = context;
		this.beanName = sessionFactoryBeanName;
		this.springTx = springManagedTransactions;
	}
	
	/**
	 * Initializes a Spring's Application context from the given path in the classpath and 
	 * retrieves the {@link SessionFactory} from that context.
	 * Use this constructor if there's only one {@link SessionFactory} in your Spring's context
	 * @param context Spring context
	 * @param springManagedTransactions indicates whether Spring manages transactions
	 */
	public SpringHibernateEntityLoaderConfig(String contextLocation, boolean springManagedTransactions) {
		this(new ClassPathXmlApplicationContext(contextLocation), springManagedTransactions);
	}
	
	/**
	 * Initializes a Spring's Application context from the given path in the classpath and 
	 * retrieves the {@link SessionFactory} from that context.
	 * Use this constructor if there's more than one {@link SessionFactory} in your Spring's context
	 * @param context Spring context
	 * @param springManagedTransactions indicates whether Spring manages transactions
	 */
	public SpringHibernateEntityLoaderConfig(String contextLocation, String sessionFactoryBeanName, boolean springManagedTransactions) {
		this(new ClassPathXmlApplicationContext(contextLocation), sessionFactoryBeanName, springManagedTransactions);
	}
	
	@Override
	public SessionFactory getSessionFactory() {
		return ((this.beanName != null)?(SessionFactory) context.getBean(beanName): context.getBean(SessionFactory.class));
	}
	
	@Override
	public boolean clientManagedTransactions() {
		return this.springTx;
	}
}
