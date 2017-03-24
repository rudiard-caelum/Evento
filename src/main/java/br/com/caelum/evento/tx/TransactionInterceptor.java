package br.com.caelum.evento.tx;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;

@Interceptor
@Transactional
public class TransactionInterceptor implements Serializable {

	private static final long serialVersionUID = -8225735107244577054L;

	@Inject
	private EntityManager manager;

	@AroundInvoke
	public Object intercept(InvocationContext ctx) throws Exception {
		Object resultado = null;
		try {
			manager.getTransaction().begin();
			resultado = ctx.proceed();
			manager.getTransaction().commit();
		} catch (Exception ex) {
			if (manager != null && manager.getTransaction().isActive()) {
				manager.getTransaction().rollback();
			}
		}
		return resultado;
	}

}
