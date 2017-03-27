package br.com.caelum.evento.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

	public GenericDAO(Class<T> clazz, EntityManager manager) {
		this.clazz = clazz;
		this.manager = manager;
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
		} catch (NoResultException re) {
			return null;
		} catch (RuntimeException re) {
			re.printStackTrace();
			return null;
		}
	}

	public List<T> lista() {
		try {
			CriteriaQuery<T> query = this.manager.getCriteriaBuilder().createQuery(clazz);
			query.select(query.from(clazz));
			List<T> lista = this.manager.createQuery(query).getResultList();
			return lista;
		} catch (NoResultException re) {
			return null;
		} catch (RuntimeException re) {
			re.printStackTrace();
			return null;
		}
	}

}
