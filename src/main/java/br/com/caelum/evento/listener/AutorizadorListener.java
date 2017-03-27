package br.com.caelum.evento.listener;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;

import org.omnifaces.util.Messages;

import br.com.caelum.evento.mb.UsuarioLogadoBean;

public class AutorizadorListener implements PhaseListener {

	private static final long serialVersionUID = -6063867775342164658L;

	@Inject
	private UsuarioLogadoBean usuarioLogado;

	@Inject
	private FacesContext context;

	@Inject
	private NavigationHandler handler;

	public void afterPhase(PhaseEvent event) {
		if (context.getViewRoot().getViewId().contains("/publico/")) {
			return;
		}
		if ("/index.xhtml".equals(context.getViewRoot().getViewId())) {
			return;
		}
		if (!usuarioLogado.isLogado()) {
			Messages.addFlashGlobalError("Para acessar esta transação é necessário logar no sistema.");
			handler.handleNavigation(context, null, "/publico/login.xhtml?faces-redirect=true");
			context.renderResponse();
		}
	}

	public void beforePhase(PhaseEvent event) {
		return;
	}

	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
