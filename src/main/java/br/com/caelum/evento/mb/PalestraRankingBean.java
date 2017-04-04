package br.com.caelum.evento.mb;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.caelum.evento.annotation.ViewModel;
import br.com.caelum.evento.dao.PalestraRankingDAO;
import br.com.caelum.evento.domain.PalestraRanking;

@ViewModel
public class PalestraRankingBean implements Serializable {

	private static final long serialVersionUID = 1777316372332165405L;

	@Inject
	private PalestraRankingDAO palestraRankingDAO;

	private List<PalestraRanking> palestrasRanking;

	public List<PalestraRanking> getPalestrasRanking() {
		if (this.palestrasRanking == null) {
			this.palestrasRanking = this.palestraRankingDAO.lista();
		}
		return this.palestrasRanking;
	}

}
