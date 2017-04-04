package br.com.caelum.evento.dao;

import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.joda.time.LocalDate;
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

	private Usuario usuarioEvento;
	private Evento evento;

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

	private String randomNumber() {
		Random rdm = new Random();
		Integer inteiro = rdm.nextInt();
		return inteiro.toString();
	}

	private void setEvento() {
		this.usuarioEvento = new Usuario("TESTE_" + randomNumber(), "teste@teste.com", "123");
		this.evento = new Evento("TESTE_" + randomNumber(), "DESCRICAO DO EVENTO", "www.caelum.com.br",
				this.usuarioEvento, "LOCAL", "LOGO", new LocalDate(), true);
	}

	@Test
	public void deveInserirEvento() {
		this.setEvento();
		this.usuarioDAO.adiciona(this.usuarioEvento);
		Assert.assertNotNull(this.usuarioEvento.getId());
		this.eventoDAO.adiciona(this.evento);
		Assert.assertNotNull(this.evento.getId());
	}

	@Test
	public void deveAlterarDescricaoEvento() {
		this.deveInserirEvento();
		this.evento.setDescricao("NOVA DESCRICAO");
		this.eventoDAO.altera(this.evento);
		Assert.assertEquals("NOVA DESCRICAO", this.evento.getDescricao());
	}

	@Test
	public void deveExcluirEvento() {
		this.deveInserirEvento();
		this.eventoDAO.remove(this.evento);
		this.evento = (Evento) this.eventoDAO.buscaString(this.nomeEvento, "nome");
		Assert.assertNull(this.evento);
		this.usuarioDAO.remove(this.usuarioEvento);
		Assert.assertNull(this.usuarioDAO.buscaString(this.usuarioEvento.getNome(), "nome"));
	}

	@Test
	public void deveBuscarIdEvento() {
		this.deveInserirEvento();
		Assert.assertNotNull(this.eventoDAO.buscaId(this.evento.getId()));
	}

	@Test
	public void deveBuscarStringNomeEvento() {
		this.deveInserirEvento();
		Assert.assertNotNull(this.eventoDAO.buscaString(this.evento.getNome(), "nome"));
	}

	@Test
	public void deveListarEventos() {
		this.deveInserirEvento();
		Assert.assertNotEquals(0, this.eventoDAO.lista().size());
	}

}
