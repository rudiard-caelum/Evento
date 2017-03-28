package br.com.caelum.evento.mb;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.hibernate.exception.ConstraintViolationException;
import org.omnifaces.util.Messages;

import br.com.caelum.evento.annotation.ViewModel;
import br.com.caelum.evento.dao.UsuarioDAO;
import br.com.caelum.evento.domain.Usuario;
import br.com.caelum.evento.tx.Transactional;

@ViewModel
public class UsuarioBean implements Serializable {

	private static final long serialVersionUID = -3993830543068917032L;

	@Inject
	private UsuarioDAO usuarioDAO;

	private Usuario usuario = new Usuario();
	private List<Usuario> usuarios;
	private String confirmaSenha;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
				Messages.addGlobalError("Senhas não coincidem.");
				return;
			}
		}
		if (!this.usuario.getSenha().equals(this.getConfirmaSenha())) {
			Messages.addGlobalError("Senhas não coincidem.");
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
			Messages.addGlobalInfo("Usuário salvo com sucesso.");
		} catch (PersistenceException | ConstraintViolationException ce) {
			Messages.addFlashGlobalError("Registro já existe.");
		} catch (Exception e) {
			Messages.addFlashGlobalError("Ocorreu um erro ao salvar o registro.");
			e.printStackTrace();
		}
	}

	@Transactional
	public void remove(Usuario usuario) {
		try {
			this.usuarioDAO.remove(usuario);
			this.usuarios = this.usuarioDAO.lista();
			Messages.addGlobalInfo("Usuário excluído com sucesso.");
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
		this.usuario = new Usuario();
	}

}
