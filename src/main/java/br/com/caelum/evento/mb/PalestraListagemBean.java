package br.com.caelum.evento.mb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import br.com.caelum.evento.annotation.ViewModel;
import br.com.caelum.evento.dao.ComentarioDAO;
import br.com.caelum.evento.dao.PalestraDAO;
import br.com.caelum.evento.dataModel.DataModelPalestra;
import br.com.caelum.evento.domain.Comentario;
import br.com.caelum.evento.domain.Palestra;
import br.com.caelum.evento.domain.VotacaoEnum;

@ViewModel
public class PalestraListagemBean implements Serializable {

	private static final long serialVersionUID = -4645175118490386728L;

	@Inject
	private PalestraDAO palestraDAO;

	@Inject
	private ComentarioDAO comentarioDAO;

	@Inject
	private DataModelPalestra dataModel;

	private List<Palestra> palestras;

	private List<Comentario> comentarios;

	public List<Palestra> getPalestras() {
		if (this.palestras == null) {
			this.palestras = this.palestraDAO.lista();
		}
		return this.palestras;
	}

	public List<Comentario> getComentarios() {
		return this.comentarios;
	}

	public DataModelPalestra getDataModel() {
		return dataModel;
	}

	public void showComentarios(ActionEvent evento) {
		Palestra palestra = (Palestra) evento.getComponent().getAttributes().get("palestraSelecionada");
		this.comentarios = comentarioDAO.listaComentariosPalestra(palestra);
	}

	public Long votosPositivos(Palestra palestra) {
		return palestraDAO.totalVotacaoPalestraETipoVoto(palestra, VotacaoEnum.POSITIVO);
	}

	public Long votosNegativos(Palestra palestra) {
		return palestraDAO.totalVotacaoPalestraETipoVoto(palestra, VotacaoEnum.NEGATIVO);
	}

	public BigDecimal porcentagemAceitacao(Palestra palestra) {
		Long votosP = this.votosPositivos(palestra);
		Long votosN = this.votosNegativos(palestra);
		BigDecimal votosPositivos = (votosP == null ? new BigDecimal(0) : BigDecimal.valueOf(votosP));
		BigDecimal votosNegativos = (votosN == null ? new BigDecimal(0) : BigDecimal.valueOf(votosN));
		return palestraDAO.porcentagemAceitacaoDaPalestra(votosPositivos, votosNegativos);
	}

	public String acessarSite(Palestra palestra) {
		String siteEvento = palestra.getEvento().getSite();
		siteEvento = (siteEvento.toUpperCase().startsWith("HTTP://") ? siteEvento : "http://" + siteEvento);
		return siteEvento;
	}

}
