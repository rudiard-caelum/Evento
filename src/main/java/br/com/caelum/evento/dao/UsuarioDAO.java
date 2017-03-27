package br.com.caelum.evento.dao;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.caelum.evento.domain.Usuario;

public class UsuarioDAO extends GenericDAO<Usuario> implements Serializable {

	private static final long serialVersionUID = -6670798470507434621L;

	@Inject
	private EntityManager manager;

	public UsuarioDAO() {
		super(Usuario.class);
	}

	public UsuarioDAO(EntityManager manager) {
		super(Usuario.class, manager);
	}

	public boolean existeUsuario(Usuario usuario) {
		Query query = manager
						.createQuery("select u from Usuario u where u.nome = :pNome and u.senha = :pSenha")
						.setParameter("pNome", usuario.getNome())
						.setParameter("pSenha", usuario.getSenha());
		boolean encontrado = !query.getResultList().isEmpty();
		return encontrado;
	}

}
