package br.com.caelum.evento.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {

	private static EntityManagerFactory factory = Persistence.createEntityManagerFactory("eventoDB");

	public EntityManager getEntityManager() {
		return factory.createEntityManager();
	}

}
