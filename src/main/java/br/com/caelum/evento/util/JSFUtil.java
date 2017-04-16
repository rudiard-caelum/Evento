package br.com.caelum.evento.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

public class JSFUtil {

	private static final String BUNDLE_NAME = "br.com.caelum.evento.mensagens.Mensagens";

	public static String getMensagem(String key) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
		return bundle.getString(key);
	}

	public static String getMensagem(String key, Object... parametros) {
		String mensagem = getMensagem(key);
		MessageFormat formatter = new MessageFormat(mensagem);
		return formatter.format(parametros);
	}

	public static String getMensagem(Locale locale, String key) {
		ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
		return bundle.getString(key);
	}

	public static String getMensagem(Locale locale, String key, Object... parametros) {
		String mensagem = getMensagem(locale, key);
		MessageFormat formatter = new MessageFormat(mensagem);
		return formatter.format(parametros);
	}

}
