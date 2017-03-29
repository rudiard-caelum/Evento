package br.com.caelum.evento.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import br.com.caelum.evento.dao.EventoDAO;
import br.com.caelum.evento.domain.Evento;

@FacesConverter(forClass = Evento.class)
public class EventoConverter implements Converter {

	@Inject
	private EventoDAO eventoDAO;

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent comp, String value) {
		if (value != null && value.trim().length() > 0) {
			try {
				Long id = Long.parseLong(value);
				return eventoDAO.buscaId(id);
			} catch (Exception e) {
				throw new ConverterException("Não foi possível encontrar o Evento: " + value);
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent comp, Object value) {
		if (value != null) {
			Evento evento = (Evento) value;
			return String.valueOf(evento.getId());
		}
		return "";
	}

}
