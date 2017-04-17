package br.com.caelum.evento.mb;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.hibernate.exception.ConstraintViolationException;
import org.omnifaces.util.Messages;

import br.com.caelum.evento.annotation.ViewModel;
import br.com.caelum.evento.dao.UsuarioDAO;
import br.com.caelum.evento.dataModel.DataModelUsuario;
import br.com.caelum.evento.domain.Usuario;
import br.com.caelum.evento.tx.Transactional;
import br.com.caelum.evento.util.JSFUtil;

@ViewModel
public class UsuarioBean implements Serializable {

	private static final long serialVersionUID = -2273796213350860510L;

	@Inject
	private UsuarioDAO usuarioDAO;

	@Inject
	private DataModelUsuario dataModel;

	private Usuario usuario = new Usuario();
	private List<Usuario> usuarios;
	private String confirmaSenha;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public DataModelUsuario getDataModel() {
		return dataModel;
	}

	public List<Usuario> getUsuarios() {
		if (this.usuarios == null) {
			this.usuarios = this.usuarioDAO.lista();
		}
		return this.usuarios;
	}

	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}

	public void novo() {
		this.limpaJSF();
	}

	@Transactional
	public void salvar() {
		if (!this.usuario.getSenha().isEmpty()) {
			if (this.usuario.confirmaSenha(this.getConfirmaSenha()) != false) {
				Messages.addGlobalError(JSFUtil.getMensagem("usuarioSenhaNaoCoincidem"));
				return;
			}
		}
		if (!this.usuario.getSenha().equals(this.getConfirmaSenha())) {
			Messages.addGlobalError(JSFUtil.getMensagem("usuarioSenhaNaoCoincidem"));
			return;
		}
		try {
			if (this.usuario.getId() == null) {
				this.usuarioDAO.adiciona(this.usuario);
			} else {
				if (this.getConfirmaSenha().isEmpty()) {
					Usuario senhaAntiga = new Usuario();
					senhaAntiga = usuarioDAO.buscaId(usuario.getId());
					this.usuario.setSenha(senhaAntiga.getSenha());
				}
				this.usuarioDAO.altera(this.usuario);
			}
			this.limpaJSF();
			this.usuarios = this.usuarioDAO.lista();
			Messages.addGlobalInfo(JSFUtil.getMensagem("usuarioConfSalvar"));
		} catch (PersistenceException | ConstraintViolationException ce) {
			Messages.addFlashGlobalError(JSFUtil.getMensagem("registroJaExiste"));
		} catch (Exception e) {
			Messages.addFlashGlobalError(JSFUtil.getMensagem("operacaoErroSalvar"));
			e.printStackTrace();
		}
	}

	@Transactional
	public void remove(Usuario usuario) {
		try {
			this.usuarioDAO.remove(usuario);
			this.usuarios = this.usuarioDAO.lista();
			Messages.addGlobalInfo(JSFUtil.getMensagem("usuarioExcluido"));
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
		this.usuario = new Usuario();
	}

}
