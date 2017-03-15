package br.com.caelum.evento.dao;

import java.io.Serializable;

import br.com.caelum.evento.domain.Usuario;

public class UsuarioDAO extends GenericDAO<Usuario> implements Serializable {

	private static final long serialVersionUID = -6670798470507434621L;

	public UsuarioDAO() {
		super(Usuario.class);
	}

}
