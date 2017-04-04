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

import br.com.caelum.evento.domain.Usuario;

public class UsuarioDAOTest {

	private static EntityManagerFactory emf;
	private EntityManager manager;

	private Usuario usuario;

	private UsuarioDAO usuarioDAO;

	@BeforeClass
	public static void beforeClass() {
		emf = Persistence.createEntityManagerFactory("testeDB");
	}

	@Before
	public void before() {
		this.manager = emf.createEntityManager();
		this.manager.getTransaction().begin();
		this.usuarioDAO = new UsuarioDAO(manager);
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

	private void setUsuario(Usuario usr) {
		this.usuario = new Usuario("TESTE_" + randomNumber(), "teste@teste.com", "123");
	}

	@Test
	public void deveInserirUsuario() {
		this.setUsuario(this.usuario);
		this.usuarioDAO.adiciona(this.usuario);
		Assert.assertNotNull(this.usuario.getId());
	}

	@Test
	public void deveAlterarEmaildoUsuario() {
		this.deveInserirUsuario();
		this.usuario.setEmail("altera@teste.com");
		this.usuarioDAO.altera(this.usuario);
		Assert.assertEquals("altera@teste.com", this.usuario.getEmail());
	}

	@Test
	public void deveExcluirUsuario() {
		this.deveInserirUsuario();
		this.usuarioDAO.remove(this.usuario);
		Assert.assertNull(this.usuarioDAO.buscaString(this.usuario.getNome(), "nome"));
	}

	@Test
	public void deveBuscarIdUsario() {
		this.deveInserirUsuario();
		Assert.assertNotNull(this.usuarioDAO.buscaId(this.usuario.getId()));
	}

	@Test
	public void deveBuscarStringNomeUsuario() {
		this.deveInserirUsuario();
		Assert.assertNotNull(this.usuarioDAO.buscaString(this.usuario.getNome(), "nome"));
	}

	@Test
	public void deveListarUsuarios() {
		this.deveInserirUsuario();
		Assert.assertNotEquals(0, this.usuarioDAO.lista().size());
	}

	@Test
	public void deveEncontrarUsuario() {
		this.deveInserirUsuario();
		Assert.assertNotNull(this.usuarioDAO.existeUsuario(this.usuario));
	}

}
