package br.com.caelum.evento.mb;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

import org.omnifaces.util.Messages;

import br.com.caelum.evento.annotation.ViewModel;
import br.com.caelum.evento.dao.PalestraDAO;
import br.com.caelum.evento.dao.VotacaoDAO;
import br.com.caelum.evento.domain.Palestra;
import br.com.caelum.evento.domain.Votacao;
import br.com.caelum.evento.tx.Transactional;

@ViewModel
public class VotacaoBean implements Serializable {

	private static final long serialVersionUID = -3134717978158320340L;

	@Inject
	private UsuarioLogadoBean usuarioLogado;

	@Inject
	private PalestraDAO palestraDAO;

	@Inject
	private VotacaoDAO votacaoDAO;

	private Palestra palestra = new Palestra();
	private Votacao votacao = new Votacao();

	private List<Palestra> palestrasAvaliadas;
	private List<Palestra> palestrasNaoAvaliadas;

	public Palestra getPalestra() {
		return palestra;
	}

	public void setPalestra(Palestra palestra) {
		this.palestra = palestra;
	}

	public Votacao getVotacao() {
		return votacao;
	}

	public void setVotacao(Votacao votacao) {
		this.votacao = votacao;
	}

	public List<Palestra> getPalestrasAvaliadas() {
		if (this.palestrasAvaliadas == null) {
			this.palestrasAvaliadas = this.votacaoDAO.listaAvaliacao("Realizada");
		}
		return this.palestrasAvaliadas;
	}

	public List<Palestra> getPalestrasNaoAvaliadas() {
		if (this.palestrasNaoAvaliadas == null) {
			this.palestrasNaoAvaliadas = this.votacaoDAO.listaAvaliacao("NaoRealizada");
		}
		return this.palestrasNaoAvaliadas;
	}

	public void editar(Long palestra) {
		this.limpaJSF();
		this.setPalestra(this.palestraDAO.buscaId(palestra));
		this.votacao = this.votacaoDAO.getVotacao(this.usuarioLogado.getUsuario(), this.getPalestra());
		if (this.votacao == null) {
			this.votacao = new Votacao();
			this.votacao.setPalestra_id(this.palestra);
			this.votacao.setUsuario_id(this.usuarioLogado.getUsuario());
			this.votacao.setVoto(this.votacao.pesoVoto(this.usuarioLogado.getUsuario()));
		}
	}

	@Transactional
	public void salvar() {
		if (this.votacao.getPalestra_id() == null) {
			Messages.addFlashGlobalError("Nenhuma palestra foi selecionada para avaliação.");
			return;
		}
		if (this.votacao.getTipoVoto() == null) {
			Messages.addFlashGlobalError("Avaliação não selecionada.");
			return;
		}
		try {
			if (!this.votacaoDAO.buscaVotoUsuario(this.votacao.getUsuario_id(), this.votacao.getPalestra_id())) {
				this.votacaoDAO.adiciona(this.votacao);
			} else {
				this.votacaoDAO.altera(this.votacao);
			}
			this.limpaJSF();
			this.palestrasNaoAvaliadas = this.votacaoDAO.listaAvaliacao("NaoRealizada");
			this.palestrasAvaliadas = this.votacaoDAO.listaAvaliacao("Realizada");
			Messages.addGlobalInfo("Avaliação realizada com sucesso. OBrigado!");
		} catch (PersistenceException | ConstraintViolationException ce) {
			Messages.addFlashGlobalError("Avaliação já realizada.");
		} catch (Exception e) {
			Messages.addFlashGlobalError("Ocorreu um erro ao salvar o registro.");
			e.printStackTrace();
		}
	}

	@Transactional
	public void remove(Long palestra) {
		try {
			this.editar(palestra);
			this.votacaoDAO.remove(votacao);
			this.limpaJSF();
			this.palestrasNaoAvaliadas = this.votacaoDAO.listaAvaliacao("NaoRealizada");
			this.palestrasAvaliadas = this.votacaoDAO.listaAvaliacao("Realizada");
			Messages.addGlobalInfo("Avaliação excluída com sucesso.");
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
		this.votacao = new Votacao();
	}

}
