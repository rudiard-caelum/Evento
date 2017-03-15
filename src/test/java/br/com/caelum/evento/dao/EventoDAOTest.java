package br.com.caelum.evento.dao;

import java.util.Random;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import br.com.caelum.evento.domain.Evento;
import br.com.caelum.evento.domain.Usuario;

public class EventoDAOTest {

	private String nomeEvento;
	private String descricaoEvento = "Descricao do Evento";
	private String siteEvento = "www.caelum.com.br";
	private Usuario usuarioEvento = new Usuario();
	private String localEvento = "Rua Vergueiro, 3185 - Vila Mariana - Sao Paulo/SP";
	private String logoEvento = "Logo";
	private DateTime dataEvento = new DateTime();
	private boolean excluiEvento = true;
	private Evento evento = new Evento();
	private UsuarioDAO usuarioDAO = new UsuarioDAO();
	private EventoDAO eventoDAO = new EventoDAO();

	private void setEvento(Evento evt) {
		Random rdm = new Random();
		Integer inteiro = rdm.nextInt();
		this.nomeEvento = "TESTE_" + inteiro.toString();
		this.usuarioEvento.setNome(this.nomeEvento);
		this.usuarioEvento.setSenha("123");
		evt.setNome(this.nomeEvento);
		evt.setDescricao(this.descricaoEvento);
		evt.setSite(this.siteEvento);
		evt.setUsuario(this.usuarioEvento);
		evt.setLocal(this.localEvento);
		evt.setLogo(this.logoEvento);
		evt.setData(this.dataEvento);
	}

	private void excluirEvento() {
		this.evento = (Evento) this.eventoDAO.buscaString(this.nomeEvento, "nome");
		if (this.evento != null) {
			eventoDAO.remove(this.evento);
			this.usuarioEvento = (Usuario) this.usuarioDAO.buscaString(this.usuarioEvento.getNome(), "nome");
			if (this.usuarioEvento != null) {
				usuarioDAO.remove(this.usuarioEvento);
			}
		}
		this.evento = new Evento();
		this.usuarioEvento = new Usuario();
	}

	@Ignore
	@Test
	public void deveInserirEvento() {
		this.setEvento(this.evento);
		this.usuarioDAO.adiciona(this.usuarioEvento);
		Assert.assertNotNull(this.usuarioEvento.getId());
		this.eventoDAO.adiciona(this.evento);
		Assert.assertNotNull(this.evento.getId());
		if (this.excluiEvento == true) {
			this.excluirEvento();
		}
	}

	@Ignore
	@Test
	public void deveAlterarDescricao() {
		this.excluiEvento = false;
		this.deveInserirEvento();
		this.evento.setDescricao("Nova Descricao");
		this.eventoDAO.altera(this.evento);
		Assert.assertEquals("Nova Descricao", this.evento.getDescricao());
		this.excluirEvento();
	}

	@Test
	public void deveExcluirEvento() {
		this.excluiEvento = false;
		this.deveInserirEvento();
		this.eventoDAO.remove(this.evento);
		this.evento = (Evento) this.eventoDAO.buscaString(this.nomeEvento, "nome");
		Assert.assertNull(this.evento);
		usuarioDAO.remove(this.usuarioEvento);
		this.usuarioEvento = (Usuario) this.usuarioDAO.buscaString(this.usuarioEvento.getNome(), "nome");
		Assert.assertNull(this.usuarioEvento);
	}

	@Ignore
	@Test
	public void deveBuscarId() {
		this.excluiEvento = false;
		this.deveInserirEvento();
		Assert.assertNotNull(this.eventoDAO.buscaId(this.evento.getId()));
		this.excluirEvento();
	}

	@Ignore
	@Test
	public void deveBuscarString() {
		this.excluiEvento = false;
		this.deveInserirEvento();
		Assert.assertNotNull(this.eventoDAO.buscaString(this.nomeEvento, "nome"));
		this.excluirEvento();
	}

	@Ignore
	@Test
	public void deveListarUsuarios() {
		this.excluiEvento = false;
		this.deveInserirEvento();
		Assert.assertNotEquals(0, this.eventoDAO.lista().size());
		this.excluirEvento();
	}

}
