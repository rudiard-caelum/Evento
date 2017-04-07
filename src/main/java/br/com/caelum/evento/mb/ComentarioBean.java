package br.com.caelum.evento.mb;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

import org.joda.time.LocalDate;
import org.omnifaces.util.Messages;

import br.com.caelum.evento.annotation.ViewModel;
import br.com.caelum.evento.dao.ComentarioDAO;
import br.com.caelum.evento.dao.PalestraDAO;
import br.com.caelum.evento.domain.Comentario;
import br.com.caelum.evento.domain.Palestra;
import br.com.caelum.evento.tx.Transactional;

@ViewModel
public class ComentarioBean implements Serializable {

	private static final long serialVersionUID = -389242462067891257L;

	@Inject
	private UsuarioLogadoBean usuarioLogado;

	@Inject
	private PalestraDAO palestraDAO;

	@Inject
	private ComentarioDAO comentarioDAO;

	private Palestra palestra = new Palestra();
	private Comentario comentario = new Comentario();

	private List<Palestra> palestrasComentadas;
	private List<Palestra> palestrasNaoComentadas;

	public Palestra getPalestra() {
		return palestra;
	}

	public void setPalestra(Palestra palestra) {
		this.palestra = palestra;
	}

	public Comentario getComentario() {
		return comentario;
	}

	public void setComentario(Comentario comentario) {
		this.comentario = comentario;
	}

	public List<Palestra> getPalestrasComentadas() {
		if (this.palestrasComentadas == null) {
			this.palestrasComentadas = this.comentarioDAO.listaComentarioUsuario(this.usuarioLogado.getUsuario(),
					"Realizado");
		}
		return this.palestrasComentadas;
	}

	public List<Palestra> getPalestrasNaoComentadas() {
		if (this.palestrasNaoComentadas == null) {
			this.palestrasNaoComentadas = this.comentarioDAO.listaComentarioUsuario(this.usuarioLogado.getUsuario(),
					"NaoRealizado");
		}
		return this.palestrasNaoComentadas;
	}

	public void editar(Long palestra) {
		this.limpaJSF();
		this.setPalestra(this.palestraDAO.buscaId(palestra));
		this.comentario = this.comentarioDAO.getComentarioUsuario(this.usuarioLogado.getUsuario(), this.getPalestra());
		if (this.comentario == null) {
			this.comentario = new Comentario();
			this.comentario.setPalestra(this.palestra);
			this.comentario.setUsuario(this.usuarioLogado.getUsuario());
			this.comentario.setData(new LocalDate());
			this.comentario.setComentario(this.comentario.getComentario());
		}
	}

	@Transactional
	public void salvar() {
		if (this.comentario.getPalestra().getId() == null) {
			Messages.addFlashGlobalError("Nenhuma palestra foi selecionada para efetuar coment�rios.");
			return;
		}
		try {
			if (!this.comentarioDAO.buscaComentarioUsuario(this.comentario.getUsuario(),
					this.comentario.getPalestra())) {
				this.comentarioDAO.adiciona(this.comentario);
			} else {
				this.comentarioDAO.altera(this.comentario);
			}
			this.limpaJSF();
			this.palestrasNaoComentadas = this.comentarioDAO.listaComentarioUsuario(this.usuarioLogado.getUsuario(),
					"NaoRealizado");
			this.palestrasComentadas = this.comentarioDAO.listaComentarioUsuario(this.usuarioLogado.getUsuario(),
					"Realizado");
			Messages.addGlobalInfo("Coment�rios realizados com sucesso. Obrigado!");
		} catch (PersistenceException | ConstraintViolationException ce) {
			Messages.addFlashGlobalError("Coment�rio j� realizado.");
		} catch (Exception e) {
			Messages.addFlashGlobalError("Ocorreu um erro ao salvar o registro.");
			e.printStackTrace();
		}
	}

	@Transactional
	public void remove(Long palestra) {
		try {
			this.editar(palestra);
			this.comentarioDAO.remove(this.comentario);
			this.limpaJSF();
			this.palestrasNaoComentadas = this.comentarioDAO.listaComentarioUsuario(this.usuarioLogado.getUsuario(),
					"NaoRealizado");
			this.palestrasComentadas = this.comentarioDAO.listaComentarioUsuario(this.usuarioLogado.getUsuario(),
					"Realizado");
			Messages.addGlobalInfo("Coment�rio exclu�do com sucesso.");
		} catch (Exception ex) {
			Messages.addFlashGlobalError("Ocorreu um erro ao excluir o registro.");
			ex.printStackTrace();
		}
	}

	public void cancelar() {
		this.limpaJSF();
		Messages.addGlobalInfo("Opera��o cancelada.");
	}

	private void limpaJSF() {
		this.palestra = new Palestra();
		this.comentario = new Comentario();
	}

}
