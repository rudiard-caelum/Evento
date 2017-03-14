package br.com.caelum.evento.dao;

import java.util.List;

import javax.persistence.EntityManager;

@SuppressWarnings("rawtypes")
public abstract class GenericDAO<T> {

	private Class clazz;
	private EntityManager manager;

	public GenericDAO() {
	}

	public GenericDAO(EntityManager manager, Class clazz) {
		this.manager = manager;
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

	@SuppressWarnings("unchecked")
	public T busca(Integer id) {
		return (T) this.manager.find(clazz, id);
	}

	@SuppressWarnings("unchecked")
	public List<T> lista() {
		return this.manager.createQuery("select g from " + clazz.getSimpleName() + " g", clazz).getResultList();
	}

}
