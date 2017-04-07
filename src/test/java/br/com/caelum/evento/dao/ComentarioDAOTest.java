package br.com.caelum.evento.dao;

import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.caelum.evento.domain.Comentario;
import br.com.caelum.evento.domain.Evento;
import br.com.caelum.evento.domain.Palestra;
import br.com.caelum.evento.domain.Usuario;

public class ComentarioDAOTest {

	private static EntityManagerFactory emf;
	private EntityManager manager;

	private Usuario usuario;
	private Evento evento;
	private Palestra palestra;
	private Comentario palestraComentario;

	private UsuarioDAO usuarioDAO;
	private EventoDAO eventoDAO;
	private PalestraDAO palestraDAO;
	private ComentarioDAO comentarioDAO;

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
		this.palestraDAO = new PalestraDAO(manager);
		this.comentarioDAO = new ComentarioDAO(manager);
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

	private void setPalestraComentario() {
		this.usuario = new Usuario("TESTE_" + randomNumber(), "teste@teste.com", "123");
		this.evento = new Evento(this.usuario.getNome(), "DESCRICAO DO EVENTO", "www.caelum.com.br", this.usuario,
				"LOCAL", "LOGO", new LocalDate(), true);
		this.palestra = new Palestra(this.usuario, "TITULO DA PALESTRA", "DESCRICAO DA PALESTRA", this.evento);
		this.palestraComentario = new Comentario(new LocalDate(), this.usuario, this.palestra, "COMENTARIO");
	}

	private boolean inserirPalestraComentario() {
		try {
			this.usuarioDAO.adiciona(this.usuario);
		} catch (Exception ex) {
			return false;
		}
		try {
			this.eventoDAO.adiciona(this.evento);
		} catch (Exception ex) {
			return false;
		}
		try {
			this.palestraDAO.adiciona(this.palestra);
		} catch (Exception ex) {
			return false;
		}
		try {
			this.comentarioDAO.adiciona(this.palestraComentario);
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	@Test
	public void deveInserirPalestraComentario() {
		this.setPalestraComentario();
		Assert.assertTrue(this.inserirPalestraComentario());
	}

	@Test(expected = PersistenceException.class)
	public void naoDeveInserirDoisComentariosnaMesmaPalestra() throws CloneNotSupportedException {
		this.deveInserirPalestraComentario();
		Comentario palestraComentario2 = palestraComentario.clone();
		try {
			this.comentarioDAO.adiciona(palestraComentario2);
		} catch (PersistenceException ex) {
			throw new PersistenceException(ex);
		}
	}

	@Test
	public void deveAlterarComentarioDaPalestra() {
		this.deveInserirPalestraComentario();
		this.palestraComentario.setComentario("TESTE");
		this.comentarioDAO.altera(this.palestraComentario);
		Assert.assertEquals("TESTE", this.palestraComentario.getComentario());
	}

	@Test
	public void deveExcluirPalestraComentario() {
		this.deveInserirPalestraComentario();
		this.comentarioDAO.remove(this.palestraComentario);
		Assert.assertFalse(this.comentarioDAO.buscaComentarioUsuario(this.usuario, this.palestra));
	}

	@Test
	public void deveBuscarIdPalestraComentario() {
		this.deveInserirPalestraComentario();
		Assert.assertNotNull(this.comentarioDAO.buscaId(this.palestraComentario.getId()));
	}

	@Test
	public void deveListarPalestraComentario() {
		this.deveInserirPalestraComentario();
		Assert.assertNotEquals(0, this.comentarioDAO.lista().size());
	}

	@Test
	public void deveListarComentariosRealizados() {
		this.deveInserirPalestraComentario();
		Assert.assertNotEquals(0, this.comentarioDAO.listaComentarioUsuario(this.usuario, "Realizado").size());
	}

	@Test
	public void deveListarComentariosNaoRealizados() {
		this.deveInserirPalestraComentario();
		Usuario usuario = new Usuario("TESTE_" + randomNumber(), "teste@teste.com", "123");
		usuarioDAO.adiciona(usuario);
		Assert.assertNotEquals(0, this.comentarioDAO.listaComentarioUsuario(usuario, "NaoRealizado").size());
	}

	@Test
	public void deveVerificarSeUsuarioComentouPalestra() {
		this.deveInserirPalestraComentario();
		Assert.assertTrue(this.comentarioDAO.buscaComentarioUsuario(this.usuario, this.palestra));
	}

	@Test
	public void deveEncontrarComentarioDoUsuario() {
		this.deveInserirPalestraComentario();
		Assert.assertNotNull(this.comentarioDAO.getComentarioUsuario(this.usuario, this.palestra));
	}

	@Test
	public void deveListarComentariosDaPalestra() {
		this.deveInserirPalestraComentario();
		Assert.assertNotNull(this.comentarioDAO.listaComentariosPalestra(this.palestra));
	}

}
