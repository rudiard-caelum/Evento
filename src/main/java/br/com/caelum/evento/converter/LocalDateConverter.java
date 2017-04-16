package br.com.caelum.evento.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.joda.time.LocalDate;

import br.com.caelum.evento.util.JSFUtil;

@FacesConverter("LocalDateConverter")
@RequestScoped
public class LocalDateConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		LocalDate ld = null;
		try {
			ld = LocalDate.fromDateFields(df.parse(value));
		} catch (ParseException ex) {
			throw new ConverterException(JSFUtil.getMensagem("erroLocalDateConverter"));
		}
		return ld;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		LocalDate ld = (LocalDate) value;
		FacesContext ctx = FacesContext.getCurrentInstance();
		Locale locale = ctx.getViewRoot().getLocale();
		String formato = (locale.getLanguage().equals("en") ? "MM/dd/yyyy" : "dd/MM/yyyy");
		return ld.toString(formato);
	}

}
