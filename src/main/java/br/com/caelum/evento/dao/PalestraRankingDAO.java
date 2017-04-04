package br.com.caelum.evento.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaQuery;

import br.com.caelum.evento.domain.Palestra;
import br.com.caelum.evento.domain.PalestraRanking;
import br.com.caelum.evento.domain.VotacaoEnum;

public class PalestraRankingDAO extends GenericDAO<PalestraRanking> implements Serializable {

	private static final long serialVersionUID = 45235576521327995L;

	@Inject
	private EntityManager manager;

	@Inject
	private PalestraDAO palestraDAO;

	public PalestraRankingDAO() {
		super(PalestraRanking.class);
	}

	public PalestraRankingDAO(EntityManager manager) {
		super(PalestraRanking.class, manager);
		this.palestraDAO = new PalestraDAO(manager);
		this.manager = manager;
	}

	public int rankingPalestra(Palestra palestra) {
		String retorno = this.palestraDAO.rankingVotacaoPorPalestra(palestra);
		if (retorno.equals("APROVADA")) {
			return 0;
		} else if (retorno.equals("POLEMICA")) {
			return 1;
		} else if (retorno.equals("NAO AVALIADA")) {
			return 2;
		} else {
			return 3;
		}
	}

	@Override
	public List<PalestraRanking> lista() {
		List<Palestra> lista;
		List<PalestraRanking> listaRanking = new ArrayList<PalestraRanking>();
		try {
			CriteriaQuery<Palestra> query = this.manager.getCriteriaBuilder().createQuery(Palestra.class);
			query.select(query.from(Palestra.class));
			lista = this.manager.createQuery(query).getResultList();
		} catch (NoResultException re) {
			return null;
		} catch (RuntimeException re) {
			re.printStackTrace();
			return null;
		}
		listaRanking.clear();
		for (Palestra lst : lista) {
			PalestraRanking palestraRanking = new PalestraRanking();
			palestraRanking.setPalestra(this.palestraDAO.buscaId(lst.getId()));
			palestraRanking.setAvaliacao(this.palestraDAO.rankingVotacaoPorPalestra(palestraRanking.getPalestra()));
			palestraRanking.setRanking(this.rankingPalestra(palestraRanking.getPalestra()));
			palestraRanking.setQtdeVotosPositivos(this.palestraDAO
					.totalVotacaoPalestraETipoVoto(palestraRanking.getPalestra(), VotacaoEnum.POSITIVO));
			listaRanking.add(palestraRanking);
		}
		Collections.sort(listaRanking);
		return listaRanking;
	}

}
