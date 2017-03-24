package br.com.caelum.evento.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public abstract class GenericDAO<T> {

	private Class<T> clazz;

	@Inject
	private EntityManager manager;

	public GenericDAO() {
	}

	public GenericDAO(Class<T> clazz) {
		this.clazz = clazz;

	}

	public void adiciona(T entity) {
		this.manager.persist(entity);
	}

	public void altera(T entity) {
		this.manager.merge(entity);
	}

	public void remove(T entity) {
		this.manager.remove(entity);
	}

	public T buscaId(Long id) {
		return (T) this.manager.find(clazz, id);
	}

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

	public List<T> lista() {
		return this.manager.createQuery("select g from " + clazz.getSimpleName() + " g", clazz).getResultList();
	}

}
