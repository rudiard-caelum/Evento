package br.com.caelum.evento.mb;

import java.io.Serializable;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.omnifaces.util.Messages;

import br.com.caelum.evento.dao.UsuarioDAO;
import br.com.caelum.evento.domain.Usuario;
import br.com.caelum.evento.util.JSFUtil;

@Model
public class LoginBean implements Serializable {

	private static final long serialVersionUID = -4206236808879134838L;

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
			eventoLogin.fire(this.usuario);
			this.usuario = (Usuario) usuarioDAO.buscaString(this.usuario.getNome(), "nome");
			usuarioLogado.login(this.usuario);
			Messages.addGlobalInfo(JSFUtil.getMensagem("loginBemVindo"));
			return "/index.xhtml?faces-redirect=true";
		} else {
			Messages.addFlashGlobalError(JSFUtil.getMensagem("loginErro"));
			usuarioLogado.efetuaLogout();
			this.usuario = new Usuario();
			return "/publico/login.xhtml";
		}
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

}
