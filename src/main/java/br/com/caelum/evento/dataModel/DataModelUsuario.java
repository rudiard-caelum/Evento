package br.com.caelum.evento.dataModel;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.caelum.evento.dao.UsuarioDAO;
import br.com.caelum.evento.domain.Usuario;

public class DataModelUsuario extends LazyDataModel<Usuario> {

	private static final long serialVersionUID = 4365656425751137702L;

	@Inject
	UsuarioDAO usuarioDAO;

	@PostConstruct
	void inicializaTotal() {
		this.setRowCount(usuarioDAO.contaRegistros());
	}

	public List<Usuario> load(int inicio, int quantidade, String campoOrdenacao, SortOrder sentidoOrdenacao,
			Map<String, Object> filtros) {
		return usuarioDAO.listaPaginada(inicio, quantidade);
	}

}
