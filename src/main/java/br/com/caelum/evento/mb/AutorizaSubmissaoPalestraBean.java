package br.com.caelum.evento.mb;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

import br.com.caelum.evento.annotation.ViewModel;
import br.com.caelum.evento.dao.EventoDAO;
import br.com.caelum.evento.domain.Evento;
import br.com.caelum.evento.tx.Transactional;

@ViewModel
public class AutorizaSubmissaoPalestraBean implements Serializable {

	private static final long serialVersionUID = -6264552583030662283L;

	@Inject
	private EventoDAO eventoDAO;

	private Evento evento = new Evento();
	private List<Evento> eventos;

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public List<Evento> getEventos() {
		if (this.eventos == null) {
			this.eventos = this.eventoDAO.lista();
		}
		return this.eventos;
	}

	@Transactional
	public void editar(RowEditEvent event) {
		try {
			boolean valor = this.evento.isPermiteSubmissao();
			this.evento = this.eventoDAO.buscaId(((Evento) event.getObject()).getId());
			this.evento.setPermiteSubmissao(valor);
			this.eventoDAO.altera(this.evento);
			this.eventos = this.eventoDAO.lista();
			Messages.addGlobalInfo("Autorização salva com sucesso.");
		} catch (Exception e) {
			Messages.addFlashGlobalError("Ocorreu um erro ao salvar o registro.");
			e.printStackTrace();
		}
	}

	public void cancelar(RowEditEvent event) {
		this.eventos = this.eventoDAO.lista();
		Messages.addGlobalInfo("Operação cancelada.");
	}

}
