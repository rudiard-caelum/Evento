package br.com.caelum.evento.dataModel;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.caelum.evento.dao.EventoDAO;
import br.com.caelum.evento.domain.Evento;

public class DataModelEvento extends LazyDataModel<Evento> {

	private static final long serialVersionUID = 2588123912538362760L;

	@Inject
	EventoDAO eventoDAO;

	@PostConstruct
	void inicializaTotal() {
		this.setRowCount(eventoDAO.contaRegistros());
	}

	public List<Evento> load(int inicio, int quantidade, String campoOrdenacao, SortOrder sentidoOrdenacao,
			Map<String, Object> filtros) {
		return eventoDAO.listaPaginada(inicio, quantidade);
	}

}
