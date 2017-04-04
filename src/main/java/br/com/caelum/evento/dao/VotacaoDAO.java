package br.com.caelum.evento.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.caelum.evento.domain.Palestra;
import br.com.caelum.evento.domain.Usuario;
import br.com.caelum.evento.domain.Votacao;

public class VotacaoDAO extends GenericDAO<Votacao> implements Serializable {

	private static final long serialVersionUID = -7613678433557349695L;

	@Inject
	private EntityManager manager;

	public VotacaoDAO() {
		super(Votacao.class);
	}

	public VotacaoDAO(EntityManager manager) {
		super(Votacao.class, manager);
		this.manager = manager;
	}

	@SuppressWarnings("unchecked")
	public List<Palestra> listaAvaliacao(Usuario usuario, String tipo) {
		Query query = this.manager.createNamedQuery("Votacao." + tipo, Palestra.class).setParameter("pUsuarioId",
				usuario);
		List<Palestra> lista = query.getResultList();
		return lista;
	}

	public boolean buscaVotoUsuario(Usuario usuario, Palestra palestra) {
		Query query = this.manager
				.createQuery("select v from Votacao v where v.usuario_id = :pUsuario and v.palestra_id = :pPalestra")
				.setParameter("pUsuario", usuario).setParameter("pPalestra", palestra);
		boolean encontrado = !query.getResultList().isEmpty();
		return encontrado;
	}

	public Votacao getVotacao(Usuario usuario, Palestra palestra) {
		try {
			TypedQuery<Votacao> query = this.manager.createQuery(
					"select v from Votacao v where v.usuario_id = :pUsuario and v.palestra_id = :pPalestra",
					Votacao.class).setParameter("pUsuario", usuario).setParameter("pPalestra", palestra);
			return query.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

}
