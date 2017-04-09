package br.com.caelum.evento.mb;

import java.io.Serializable;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import br.com.caelum.evento.annotation.ViewModel;
import br.com.caelum.evento.dao.EventoDAO;
import br.com.caelum.evento.dao.PalestraRankingDAO;
import br.com.caelum.evento.domain.Evento;
import br.com.caelum.evento.domain.PalestraRanking;

@ViewModel
public class EventoPalestraBean implements Serializable {

	private static final long serialVersionUID = 6777779726502138485L;

	@Inject
	private FacesContext facesContext;

	@Inject
	private EventoDAO eventoDAO;

	@Inject
	private PalestraRankingDAO palestraRankingDAO;

	private Evento evento = new Evento();

	private List<PalestraRanking> palestrasRanking;
	private List<Evento> eventos;

	public Evento getEvento() {
		return evento;
	}

	public List<Evento> getEventos() {
		if (this.eventos == null) {
			this.eventos = this.eventoDAO.lista();
		}
		return this.eventos;
	}

	public List<PalestraRanking> getPalestrasRanking() {
		return this.palestrasRanking;
	}

	public void showPalestrasRanking(ActionEvent eventoSelecionado) {
		Evento evento = (Evento) eventoSelecionado.getComponent().getAttributes().get("eventoSelecionado");
		this.palestrasRanking = this.palestraRankingDAO.listaPorEvento(evento);
	}

	public String submeterPalestra() {
		String parametro = facesContext.getExternalContext().getRequestParameterMap().get("eventoSelecionado");
		Evento evento = eventoDAO.buscaId(Long.parseLong(parametro));
		return "/restrito/palestra.xhtml?faces-redirect=true&eventoId=" + evento.getId();
	}

}
