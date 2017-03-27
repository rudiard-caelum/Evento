package br.com.caelum.evento.util;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

public class NavigationHandlerUtil {

	@Inject
	FacesContext context;

	@Produces
	@RequestScoped
	public NavigationHandler getNavigationHandler() {
		return context.getApplication().getNavigationHandler();

	}

}
