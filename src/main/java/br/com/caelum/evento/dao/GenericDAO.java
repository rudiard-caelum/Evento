package br.com.caelum.evento.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.caelum.evento.util.JPAUtil;

@SuppressWarnings("rawtypes")
public abstract class GenericDAO<T> {

	private Class clazz;
	private EntityManager manager = new JPAUtil().getEntityManager();

	public GenericDAO() {
	}

	public GenericDAO(Class clazz) {
		this.clazz = clazz;

	}

	public void adiciona(T entity) {
		this.manager.getTransaction().begin();
		this.manager.persist(entity);
		this.manager.getTransaction().commit();
	}

	public void altera(T entity) {
		this.manager.getTransaction().begin();
		this.manager.merge(entity);
		this.manager.getTransaction().commit();
	}

	public void remove(T entity) {
		this.manager.getTransaction().begin();
		this.manager.remove(entity);
		this.manager.getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	public T buscaId(Long id) {
		return (T) this.manager.find(clazz, id);
	}

	@SuppressWarnings("unchecked")
	public Object buscaString(String valor, String campo) {
		try {
			CriteriaBuilder builder = this.manager.getCriteriaBuilder();
			CriteriaQuery<T> criteria = builder.createQuery(this.clazz);
			Root<T> from = criteria.from(this.clazz);
			Predicate condition = builder.equal(from.get(campo), valor);
			criteria.where(condition);
			Object result = this.manager.createQuery(criteria).getSingleResult();
			return result;
		} catch (RuntimeException re) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> lista() {
		return this.manager.createQuery("select g from " + clazz.getSimpleName() + " g", clazz).getResultList();
	}

}
