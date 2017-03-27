package br.com.caelum.evento.mb;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.evento.domain.Usuario;

@Named
@SessionScoped
public class UsuarioLogadoBean implements Serializable {

	private static final long serialVersionUID = -6726518892361052986L;

	@Inject
	FacesContext context;

	private Usuario usuario;

	public void login(Usuario usuario) {
		this.usuario = usuario;
	}

	public String efetuaLogout() {
		if (this.isLogado()) {
			this.usuario = null;
			context.getExternalContext().invalidateSession();
		}
		return "/index.xhtml?faces-redirect=true";
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public boolean isLogado() {
		return usuario != null;
	}

}
