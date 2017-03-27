package br.com.caelum.evento.util;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@ApplicationScoped
public class EntityManagerProducer {

	@PersistenceUnit(unitName = "eventoDB")
	private EntityManagerFactory emf;

	@Produces
	@RequestScoped
	public EntityManager getEntityManager() {
		try {
			EntityManager manager = emf.createEntityManager();
			return manager;
		} catch (Exception | Error ex) {
			throw new RuntimeException(ex);
		}
	}

	public void close(@Disposes EntityManager manager) {
		if (manager != null) {
			manager.close();
		}
	}

}
