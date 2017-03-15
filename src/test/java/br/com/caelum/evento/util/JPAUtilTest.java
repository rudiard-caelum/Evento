package br.com.caelum.evento.util;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Test;

public class JPAUtilTest {

	@Test
	public void deveCriaEntityManager() {
		EntityManager manager = new JPAUtil().getEntityManager();
		Assert.assertNotNull(manager);
		manager.close();
	}

}
