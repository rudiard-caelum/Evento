package br.com.caelum.evento.mb;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.hibernate.exception.ConstraintViolationException;
import org.omnifaces.util.Messages;

import br.com.caelum.evento.annotation.ViewModel;
import br.com.caelum.evento.dao.EventoDAO;
import br.com.caelum.evento.domain.Evento;
import br.com.caelum.evento.tx.Transactional;
import br.com.caelum.evento.util.JSFUtil;

@ViewModel
public class EventoBean implements Serializable {

	private static final long serialVersionUID = -4000163531787453239L;

	@Inject
	private UsuarioLogadoBean usuarioLogado;

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

	public List<Evento> busca(String texto) {
		return eventoDAO.buscaPorNome(texto);
	}

	public void novo() {
		this.limpaJSF();
	}

	@Transactional
	public void salvar() {
		if (this.evento.getUsuario() == null) {
			this.evento.setUsuario(usuarioLogado.getUsuario());
		}
		try {
			if (this.evento.getId() == null) {
				this.eventoDAO.adiciona(this.evento);
			} else {
				this.eventoDAO.altera(this.evento);
			}
			this.limpaJSF();
			this.eventos = this.eventoDAO.lista();
			Messages.addGlobalInfo(JSFUtil.getMensagem("eventoConfSalvar"));
		} catch (PersistenceException | ConstraintViolationException ce) {
			Messages.addFlashGlobalError(JSFUtil.getMensagem("registroJaExiste"));
		} catch (Exception e) {
			Messages.addFlashGlobalError(JSFUtil.getMensagem("operacaoErroSalvar"));
			e.printStackTrace();
		}
	}

	@Transactional
	public void remove(Evento evento) {
		try {
			this.eventoDAO.remove(evento);
			this.eventos = this.eventoDAO.lista();
			Messages.addGlobalInfo(JSFUtil.getMensagem("eventoExcluido"));
		} catch (Exception ex) {
			Messages.addFlashGlobalError(JSFUtil.getMensagem("operacaoErroExcluir"));
			ex.printStackTrace();
		}
	}

	public void cancelar() {
		this.limpaJSF();
		Messages.addGlobalInfo(JSFUtil.getMensagem("operacaoCancelada"));
	}

	private void limpaJSF() {
		this.evento = new Evento();
	}

}
