package br.com.caelum.evento.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.caelum.evento.util.JSFUtil;

@FacesConverter("BooleanConverter")
public class BooleanConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value == null) {
			return Boolean.FALSE;
		}
		return JSFUtil.getMensagem("booleanYes").equalsIgnoreCase(value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value == null) {
			return JSFUtil.getMensagem("booleanNo");
		}
		if (value instanceof Boolean) {
			return ((Boolean) value).booleanValue() ? JSFUtil.getMensagem("booleanYes")
					: JSFUtil.getMensagem("booleanNo");
		}
		return value.toString();
	}

}
