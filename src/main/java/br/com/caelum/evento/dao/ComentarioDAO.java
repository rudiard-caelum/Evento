package br.com.caelum.evento.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.caelum.evento.domain.Comentario;
import br.com.caelum.evento.domain.Palestra;
import br.com.caelum.evento.domain.Usuario;

public class ComentarioDAO extends GenericDAO<Comentario> implements Serializable {

	private static final long serialVersionUID = -1504646177514130696L;

	@Inject
	private EntityManager manager;

	public ComentarioDAO() {
		super(Comentario.class);
	}

	public ComentarioDAO(EntityManager manager) {
		super(Comentario.class, manager);
		this.manager = manager;
	}

	@SuppressWarnings("unchecked")
	public List<Palestra> listaComentarioUsuario(Usuario usuario, String tipo) {
		Query query = this.manager.createNamedQuery("PalestraComentario." + tipo, Palestra.class)
				.setParameter("pUsuario", usuario);
		List<Palestra> lista = query.getResultList();
		return lista;
	}

	public boolean buscaComentarioUsuario(Usuario usuario, Palestra palestra) {
		Query query = this.manager
				.createQuery("select c from Comentario c where c.usuario = :pUsuario and c.palestra = :pPalestra")
				.setParameter("pUsuario", usuario).setParameter("pPalestra", palestra);
		boolean encontrado = !query.getResultList().isEmpty();
		return encontrado;
	}

	public Comentario getComentarioUsuario(Usuario usuario, Palestra palestra) {
		try {
			TypedQuery<Comentario> query = this.manager
					.createQuery("select c from Comentario c where c.usuario = :pUsuario and c.palestra = :pPalestra",
							Comentario.class)
					.setParameter("pUsuario", usuario).setParameter("pPalestra", palestra);
			return query.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

	public List<Comentario> listaComentariosPalestra(Palestra palestra) {
		try {
			TypedQuery<Comentario> query = this.manager
					.createQuery("select c from Comentario c where c.palestra = :pPalestra", Comentario.class)
					.setParameter("pPalestra", palestra);
			return query.getResultList();
		} catch (NoResultException nre) {
			return null;
		}
	}

}
