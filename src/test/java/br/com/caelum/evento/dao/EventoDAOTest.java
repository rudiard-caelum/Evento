package br.com.caelum.evento.dao;

import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.caelum.evento.domain.Evento;
import br.com.caelum.evento.domain.Usuario;

public class EventoDAOTest {

	private static EntityManagerFactory emf;
	private EntityManager manager;

	private String nomeEvento;
	private boolean excluiEvento = true;

	private Usuario usuarioEvento = new Usuario();
	private Evento evento = new Evento();

	private UsuarioDAO usuarioDAO;
	private EventoDAO eventoDAO;

	@BeforeClass
	public static void beforeClass() {
		emf = Persistence.createEntityManagerFactory("testeDB");
	}

	@Before
	public void before() {
		this.manager = emf.createEntityManager();
		this.manager.getTransaction().begin();
		this.usuarioDAO = new UsuarioDAO(manager);
		this.eventoDAO = new EventoDAO(manager);
	}

	@After
	public void after() {
		this.manager.getTransaction().rollback();
		this.manager.close();
	}

	@AfterClass
	public static void afterClass() {
		emf.close();
	}

	private void setEvento(Evento evt) {
		Random rdm = new Random();
		Integer inteiro = rdm.nextInt();
		this.nomeEvento = "TESTE_" + inteiro.toString();
		this.usuarioEvento.setNome(this.nomeEvento);
		this.usuarioEvento.setSenha("123");
		evt.setNome(this.nomeEvento);
		evt.setDescricao("DESCRICAO DO EVENTO");
		evt.setSite("www.caelum.com.br");
		evt.setUsuario(this.usuarioEvento);
		evt.setLocal("ENDERECO DO EVENTO");
		evt.setLogo("LOGO");
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

	@Test
	public void deveAlterarDescricaoEvento() {
		this.excluiEvento = false;
		this.deveInserirEvento();
		this.evento.setDescricao("NOVA DESCRICAO");
		this.eventoDAO.altera(this.evento);
		Assert.assertEquals("NOVA DESCRICAO", this.evento.getDescricao());
		this.excluirEvento();
	}

	@Test
	public void deveExcluirEvento() {
		this.excluiEvento = false;
		this.deveInserirEvento();
		this.eventoDAO.remove(this.evento);
		this.evento = (Evento) this.eventoDAO.buscaString(this.nomeEvento, "nome");
		Assert.assertNull(this.evento);
		this.usuarioDAO.remove(this.usuarioEvento);
		Assert.assertNull(this.usuarioDAO.buscaString(this.usuarioEvento.getNome(), "nome"));
	}

	@Test
	public void deveBuscarIdEvento() {
		this.excluiEvento = false;
		this.deveInserirEvento();
		Assert.assertNotNull(this.eventoDAO.buscaId(this.evento.getId()));
		this.excluirEvento();
	}

	@Test
	public void deveBuscarStringNomeEvento() {
		this.excluiEvento = false;
		this.deveInserirEvento();
		Assert.assertNotNull(this.eventoDAO.buscaString(this.nomeEvento, "nome"));
		this.excluirEvento();
	}

	@Test
	public void deveListarEventos() {
		this.excluiEvento = false;
		this.deveInserirEvento();
		Assert.assertNotEquals(0, this.eventoDAO.lista().size());
		this.excluirEvento();
	}

}
