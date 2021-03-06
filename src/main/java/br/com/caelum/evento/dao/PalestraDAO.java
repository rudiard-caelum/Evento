package br.com.caelum.evento.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.caelum.evento.domain.Evento;
import br.com.caelum.evento.domain.Palestra;
import br.com.caelum.evento.domain.VotacaoEnum;

public class PalestraDAO extends GenericDAO<Palestra> implements Serializable {

	private static final long serialVersionUID = 2862631713555110168L;

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

	public List<Palestra> getPalestraEvento(Evento evento) {
		try {
			TypedQuery<Palestra> query = this.manager
					.createQuery("select p from Palestra p where p.evento = :pEvento", Palestra.class)
					.setParameter("pEvento", evento);
			System.out.println(query.getResultList().size());
			return query.getResultList();
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

	public BigDecimal porcentagemAceitacaoDaPalestra(BigDecimal votosPositivos, BigDecimal votosNegativos) {
		BigDecimal totalVotos = votosPositivos.add(votosNegativos);
		return (totalVotos.equals(new BigDecimal(0)) ? new BigDecimal(0)
				: (BigDecimal) votosPositivos.divide(totalVotos, 2, RoundingMode.HALF_EVEN)
						.multiply(new BigDecimal(100)));
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
		BigDecimal porcentagemPositivo = this.porcentagemAceitacaoDaPalestra(votosPositivos, votosNegativos);

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
