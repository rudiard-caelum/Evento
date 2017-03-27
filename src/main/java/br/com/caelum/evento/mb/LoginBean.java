package br.com.caelum.evento.mb;

import java.io.Serializable;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.omnifaces.util.Messages;

import br.com.caelum.evento.dao.UsuarioDAO;
import br.com.caelum.evento.domain.Usuario;

@Model
public class LoginBean implements Serializable {

	private static final long serialVersionUID = -8364432914534199930L;

	@Inject
	Event<Usuario> eventoLogin;

	@Inject
	private UsuarioLogadoBean usuarioLogado;

	@Inject
	private UsuarioDAO usuarioDAO;

	private Usuario usuario = new Usuario();

	public String efetuaLogin() {
		boolean loginValido = usuarioDAO.existeUsuario(this.usuario);
		if (loginValido) {
			eventoLogin.fire(usuario);
			usuarioLogado.login(usuario);
			Messages.addGlobalInfo("Bem-vindo ao sistema.");
			return "/index.xhtml?faces-redirect=true";
		} else {
			Messages.addFlashGlobalError("Usuário ou Senha inválidos.");
			usuarioLogado.efetuaLogout();
			this.usuario = new Usuario();
			return "/publico/login.xhtml";
		}
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

}
