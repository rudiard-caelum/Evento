package br.com.caelum.evento.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;

import br.com.caelum.evento.domain.Evento;

public class EventoDAO extends GenericDAO<Evento> implements Serializable {

	private static final long serialVersionUID = -4976046817058319999L;

	public EventoDAO() {
		super(Evento.class);
	}

	public EventoDAO(EntityManager manager) {
		super(Evento.class, manager);
	}

}
