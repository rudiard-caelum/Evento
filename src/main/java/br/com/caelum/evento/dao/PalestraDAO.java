package br.com.caelum.evento.dao;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.caelum.evento.domain.Evento;
import br.com.caelum.evento.domain.Palestra;

public class PalestraDAO extends GenericDAO<Palestra> implements Serializable {

	private static final long serialVersionUID = 2040427863328163967L;

	@Inject
	private EntityManager manager;

	public PalestraDAO() {
		super(Palestra.class);
	}

	public PalestraDAO(EntityManager manager) {
		super(Palestra.class, manager);
	}

	public Palestra getPalestra(Evento evento, String titulo) {
		try {
			TypedQuery<Palestra> query = this.manager
					.createQuery("select p from Palestra p where p.evento = :pEvento and p.titulo = :pPalestra",
							Palestra.class)
					.setParameter("pEvento", evento).setParameter("pPalestra", titulo);
			return query.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

}
