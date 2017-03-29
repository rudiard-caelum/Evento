package br.com.caelum.evento.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.joda.time.LocalDate;

@FacesConverter("localDateConverter")
@RequestScoped
public class LocalDateConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		LocalDate ld = null;
		try {
			ld = LocalDate.fromDateFields(df.parse(value));
		} catch (ParseException parseException) {
			throw new ConverterException("Impossível converter a data.");
		}
		return ld;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		LocalDate ld = (LocalDate) value;
		return ld.toString("dd/MM/yyyy");
	}
}
