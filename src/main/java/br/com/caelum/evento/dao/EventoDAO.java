package br.com.caelum.evento.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.caelum.evento.domain.Evento;

public class EventoDAO extends GenericDAO<Evento> implements Serializable {

	private static final long serialVersionUID = -4976046817058319999L;

	@Inject
	private EntityManager manager;

	public EventoDAO() {
		super(Evento.class);
	}

	public EventoDAO(EntityManager manager) {
		super(Evento.class, manager);
	}

	public List<Evento> buscaPorNome(String nome) {
		String jpql = "select e from Evento e where lower(e.nome) like :nome order by e.nome";
		List<Evento> lista = manager.createQuery(jpql, Evento.class)
									.setParameter("nome", "%" + nome + "%").getResultList();
		return lista;
	}

}
