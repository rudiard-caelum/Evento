package br.com.caelum.evento.util;

import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;

import org.junit.Test;

public class JPAUtilTest {

	@Test
	public void deveCriaEntityManager() {
		EntityManager manager = new JPAUtil().getEntityManager();
		assertNotNull(manager);
		manager.close();
	}

}
