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
import br.com.caelum.evento.util.JSFUtil;

@ViewModel
public class VotacaoBean implements Serializable {

	private static final long serialVersionUID = 8939900226516220355L;

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
			this.palestrasAvaliadas = this.votacaoDAO.listaAvaliacao(this.usuarioLogado.getUsuario(), "Realizada");
		}
		return this.palestrasAvaliadas;
	}

	public List<Palestra> getPalestrasNaoAvaliadas() {
		if (this.palestrasNaoAvaliadas == null) {
			this.palestrasNaoAvaliadas = this.votacaoDAO.listaAvaliacao(this.usuarioLogado.getUsuario(),
					"NaoRealizada");
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
			Messages.addFlashGlobalError(JSFUtil.getMensagem("votacaoPalestraNaoSelecionada"));
			return;
		}
		if (this.votacao.getTipoVoto() == null) {
			Messages.addFlashGlobalError(JSFUtil.getMensagem("votacaoAvalicaoNaoSelecionada"));
			return;
		}
		try {
			if (!this.votacaoDAO.buscaVotoUsuario(this.votacao.getUsuario_id(), this.votacao.getPalestra_id())) {
				this.votacaoDAO.adiciona(this.votacao);
			} else {
				this.votacaoDAO.altera(this.votacao);
			}
			this.limpaJSF();
			this.palestrasNaoAvaliadas = this.votacaoDAO.listaAvaliacao(this.usuarioLogado.getUsuario(),
					"NaoRealizada");
			this.palestrasAvaliadas = this.votacaoDAO.listaAvaliacao(this.usuarioLogado.getUsuario(), "Realizada");
			Messages.addGlobalInfo(JSFUtil.getMensagem("votacaoConfAvalicao"));
		} catch (PersistenceException | ConstraintViolationException ce) {
			Messages.addFlashGlobalError(JSFUtil.getMensagem("votacaoAvaliacaoJaRealizada"));
		} catch (Exception e) {
			Messages.addFlashGlobalError(JSFUtil.getMensagem("operacaoErroSalvar"));
			e.printStackTrace();
		}
	}

	@Transactional
	public void remove(Long palestra) {
		try {
			this.editar(palestra);
			this.votacaoDAO.remove(votacao);
			this.limpaJSF();
			this.palestrasNaoAvaliadas = this.votacaoDAO.listaAvaliacao(this.usuarioLogado.getUsuario(),
					"NaoRealizada");
			this.palestrasAvaliadas = this.votacaoDAO.listaAvaliacao(this.usuarioLogado.getUsuario(), "Realizada");
			Messages.addGlobalInfo(JSFUtil.getMensagem("votacaoExcluido"));
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
		this.votacao = new Votacao();
	}

}
