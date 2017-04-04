package br.com.caelum.evento.domain;

import java.io.Serializable;

public class PalestraRanking implements Comparable<PalestraRanking>, Serializable {

	private static final long serialVersionUID = -7296099650563331816L;

	private Palestra palestra;

	private Integer ranking;

	private Long qtdeVotosPositivos;

	public PalestraRanking() {
		super();
	}

	public PalestraRanking(Palestra palestra, int ranking, Long qtdeVotosPositivos) {
		super();
		this.palestra = palestra;
		this.ranking = ranking;
		this.qtdeVotosPositivos = qtdeVotosPositivos;
	}

	@Override
	public int compareTo(PalestraRanking object) {
		if (object.getRanking() != this.getRanking()) {
			return ranking.compareTo(object.getRanking());
		} else if (object.getQtdeVotosPositivos() != this.getQtdeVotosPositivos()) {
			return Integer.compare(object.getQtdeVotosPositivos().intValue(), this.getQtdeVotosPositivos().intValue());
		}
		return ranking.compareTo(object.getRanking());
	}

	public Palestra getPalestra() {
		return palestra;
	}

	public void setPalestra(Palestra palestra) {
		this.palestra = palestra;
	}

	public Integer getRanking() {
		return ranking;
	}

	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}

	public Long getQtdeVotosPositivos() {
		return qtdeVotosPositivos;
	}

	public void setQtdeVotosPositivos(Long qtdeVotosPositivos) {
		this.qtdeVotosPositivos = qtdeVotosPositivos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((palestra == null) ? 0 : palestra.hashCode());
		result = prime * result + ((qtdeVotosPositivos == null) ? 0 : qtdeVotosPositivos.hashCode());
		result = prime * result + ((ranking == null) ? 0 : ranking.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PalestraRanking other = (PalestraRanking) obj;
		if (palestra == null) {
			if (other.palestra != null)
				return false;
		} else if (!palestra.equals(other.palestra))
			return false;
		if (qtdeVotosPositivos == null) {
			if (other.qtdeVotosPositivos != null)
				return false;
		} else if (!qtdeVotosPositivos.equals(other.qtdeVotosPositivos))
			return false;
		if (ranking == null) {
			if (other.ranking != null)
				return false;
		} else if (!ranking.equals(other.ranking))
			return false;
		return true;
	}

}
