package br.com.caelum.evento.mb;

import java.io.Serializable;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@SessionScoped
public class InternacionalizacaoBean implements Serializable {

	private static final long serialVersionUID = 5741885671439278914L;

	@Inject
	private FacesContext facesContext;

	private Locale locale;

	@PostConstruct
	private void loadDefaultLocale() {
		this.locale = facesContext.getApplication().getDefaultLocale();
	}

	public Locale getLocale() {
		return locale;
	}

	public void changeLocale(String idioma) {
		String[] info = idioma.split("_");
		this.locale = new Locale(info[0], info[1]);
		facesContext.getViewRoot().setLocale(this.locale);
	}

}
