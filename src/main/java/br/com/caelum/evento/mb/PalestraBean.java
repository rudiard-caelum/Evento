package br.com.caelum.evento.mb;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.hibernate.exception.ConstraintViolationException;
import org.omnifaces.util.Messages;

import br.com.caelum.evento.annotation.ViewModel;
import br.com.caelum.evento.dao.EventoDAO;
import br.com.caelum.evento.dao.PalestraDAO;
import br.com.caelum.evento.domain.Evento;
import br.com.caelum.evento.domain.Palestra;
import br.com.caelum.evento.tx.Transactional;
import br.com.caelum.evento.util.JSFUtil;

@ViewModel
public class PalestraBean implements Serializable {

	private static final long serialVersionUID = -8805393651999966272L;

	@Inject
	private FacesContext facesContext;

	@Inject
	private UsuarioLogadoBean usuarioLogado;

	@Inject
	private EventoDAO eventoDAO;

	@Inject
	private PalestraDAO palestraDAO;

	private Palestra palestra = new Palestra();
	private List<Palestra> palestras;

	public Palestra getPalestra() {
		return palestra;
	}

	public void setPalestra(Palestra palestra) {
		this.palestra = palestra;
	}

	public List<Palestra> getPalestras() {
		if (this.palestras == null) {
			this.palestras = this.palestraDAO.lista();
		}
		return this.palestras;
	}

	public void novo() {
		this.limpaJSF();
	}

	@PostConstruct
	public void buscaEventoUrl() {
		String parametro = facesContext.getExternalContext().getRequestParameterMap().get("eventoId");
		if (parametro != null) {
			Evento evento = eventoDAO.buscaId(Long.parseLong(parametro));
			this.palestra.setEvento(evento);
		}
	}

	@Transactional
	public void salvar() {
		if (!this.palestra.podeSubmeterPalestra()) {
			Messages.addFlashGlobalError(JSFUtil.getMensagem("palestraSubmissaoPriobida"));
			return;
		}
		if (this.palestra.getPalestrante() == null) {
			this.palestra.setPalestrante(usuarioLogado.getUsuario());
		}
		if (this.palestra.getPalestrante() == null) {
			this.palestra.setPalestrante(usuarioLogado.getUsuario());
		}
		try {
			if (this.palestra.getId() == null) {
				this.palestraDAO.adiciona(this.palestra);
			} else {
				this.palestraDAO.altera(this.palestra);
			}
			this.limpaJSF();
			this.palestras = this.palestraDAO.lista();
			Messages.addGlobalInfo(JSFUtil.getMensagem("palestraConfSalvar"));
		} catch (PersistenceException | ConstraintViolationException ce) {
			Messages.addFlashGlobalError(JSFUtil.getMensagem("registroJaExiste"));
		} catch (Exception e) {
			Messages.addFlashGlobalError(JSFUtil.getMensagem("operacaoErroSalvar"));
			e.printStackTrace();
		}
	}

	@Transactional
	public void remove(Palestra palestra) {
		try {
			this.palestraDAO.remove(palestra);
			this.palestras = this.palestraDAO.lista();
			Messages.addGlobalInfo(JSFUtil.getMensagem("palestraExcluida"));
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
		this.palestra = new Palestra();
	}

}
