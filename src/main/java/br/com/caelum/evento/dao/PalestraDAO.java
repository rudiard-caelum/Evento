package br.com.caelum.evento.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.caelum.evento.domain.Evento;
import br.com.caelum.evento.domain.Palestra;
import br.com.caelum.evento.domain.VotacaoEnum;

public class PalestraDAO extends GenericDAO<Palestra> implements Serializable {

	private static final long serialVersionUID = 2040427863328163967L;

	@Inject
	private EntityManager manager;

	public PalestraDAO() {
		super(Palestra.class);
	}

	public PalestraDAO(EntityManager manager) {
		super(Palestra.class, manager);
		this.manager = manager;
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

	public Long totalVotacaoPalestraETipoVoto(Palestra palestra, VotacaoEnum tipoVoto) {
		String jpql = "select sum(v.voto) from Votacao v where v.palestra_id = :pPalestra and v.tipoVoto = :pTipoVoto";
		TypedQuery<Long> query = this.manager.createQuery(jpql, Long.class).setParameter("pPalestra", palestra)
				.setParameter("pTipoVoto", tipoVoto);
		return query.getSingleResult();
	}

	public String rankingVotacaoPorPalestra(Palestra palestra) {
		Long votosP = this.totalVotacaoPalestraETipoVoto(palestra, VotacaoEnum.POSITIVO);
		Long votosN = this.totalVotacaoPalestraETipoVoto(palestra, VotacaoEnum.NEGATIVO);

		BigDecimal votosPositivos = (votosP == null ? new BigDecimal(0) : BigDecimal.valueOf(votosP));
		BigDecimal votosNegativos = (votosN == null ? new BigDecimal(0) : BigDecimal.valueOf(votosN));

		if ((votosPositivos.compareTo(new BigDecimal(0)) == 0) && (votosNegativos.compareTo(new BigDecimal(0)) == 0)) {
			return "NAO AVALIADA";
		}

		BigDecimal totalVotos = votosPositivos.add(votosNegativos);
		BigDecimal porcentagemPositivo = votosPositivos.divide(totalVotos, 2, RoundingMode.HALF_EVEN)
				.multiply(new BigDecimal(100));

		if ((totalVotos.compareTo(new BigDecimal(50)) >= 0) && ((porcentagemPositivo.compareTo(new BigDecimal(51)) >= 0)
				&& (porcentagemPositivo.compareTo(new BigDecimal(55)) <= 0))) {
			return "POLEMICA";
		}

		if ((totalVotos.compareTo(new BigDecimal(100)) > 0)
				&& (porcentagemPositivo.compareTo(new BigDecimal(50)) >= 1)) {
			return "APROVADA";
		} else {
			if ((votosNegativos.compareTo(new BigDecimal(0)) > 0)) {
				if (votosPositivos.divide(votosNegativos, 2, RoundingMode.HALF_EVEN)
						.compareTo(new BigDecimal(2)) >= 0) {
					return "APROVADA";
				} else {
					return "POLEMICA";
				}
			} else {
				return "APROVADA";
			}
		}

	}

}
