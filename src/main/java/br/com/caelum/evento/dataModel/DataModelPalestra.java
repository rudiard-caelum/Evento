package br.com.caelum.evento.dataModel;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.caelum.evento.dao.PalestraDAO;
import br.com.caelum.evento.domain.Palestra;

public class DataModelPalestra extends LazyDataModel<Palestra> {

	private static final long serialVersionUID = 3974620250392921848L;

	@Inject
	PalestraDAO palestraDAO;

	@PostConstruct
	void inicializaTotal() {
		this.setRowCount(palestraDAO.contaRegistros());
	}

	public List<Palestra> load(int inicio, int quantidade, String campoOrdenacao, SortOrder sentidoOrdenacao,
			Map<String, Object> filtros) {
		return palestraDAO.listaPaginada(inicio, quantidade);
	}

}
