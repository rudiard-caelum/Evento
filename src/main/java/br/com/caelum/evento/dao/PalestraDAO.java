package br.com.caelum.evento.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;

import br.com.caelum.evento.domain.Palestra;

public class PalestraDAO extends GenericDAO<Palestra> implements Serializable {

	private static final long serialVersionUID = 6915947383465602558L;

	public PalestraDAO() {
		super(Palestra.class);
	}

	public PalestraDAO(EntityManager manager) {
		super(Palestra.class, manager);
	}

}
