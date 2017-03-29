package br.com.caelum.evento.mb;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.hibernate.exception.ConstraintViolationException;
import org.omnifaces.util.Messages;

import br.com.caelum.evento.annotation.ViewModel;
import br.com.caelum.evento.dao.PalestraDAO;
import br.com.caelum.evento.domain.Palestra;
import br.com.caelum.evento.tx.Transactional;

@ViewModel
public class PalestraBean implements Serializable {

	private static final long serialVersionUID = -5459013091600644969L;

	@Inject
	private UsuarioLogadoBean usuarioLogado;

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

	@Transactional
	public void salvar() {
		if (!this.palestra.podeSubmeterPalestra()) {
			Messages.addFlashGlobalError("A submissão desta Palestra para o Evento não é permitida.");
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
			Messages.addGlobalInfo("Palestra salva com sucesso.");
		} catch (PersistenceException | ConstraintViolationException ce) {
			Messages.addFlashGlobalError("Registro já existe.");
		} catch (Exception e) {
			Messages.addFlashGlobalError("Ocorreu um erro ao salvar o registro.");
			e.printStackTrace();
		}
	}

	@Transactional
	public void remove(Palestra palestra) {
		try {
			this.palestraDAO.remove(palestra);
			this.palestras = this.palestraDAO.lista();
			Messages.addGlobalInfo("Palestra excluída com sucesso.");
		} catch (Exception ex) {
			Messages.addFlashGlobalError("Ocorreu um erro ao excluir o registro.");
			ex.printStackTrace();
		}
	}

	public void cancelar() {
		this.limpaJSF();
		Messages.addGlobalInfo("Operação cancelada.");
	}

	private void limpaJSF() {
		this.palestra = new Palestra();
	}

}
