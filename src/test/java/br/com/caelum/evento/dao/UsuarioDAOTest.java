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

	private String nomeUsuario;
	private boolean excluiUsuario = true;

	private Usuario usuario = new Usuario();

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

	private void setUsuario(Usuario usr) {
		Random rdm = new Random();
		Integer inteiro = rdm.nextInt(1000);
		this.nomeUsuario = "TESTE_" + inteiro.toString();
		usr.setNome(this.nomeUsuario);
		usr.setEmail("teste@teste.com");
		usr.setSenha("123456");
	}

	private void excluirUsuario() {
		if (this.usuario != null) {
			usuarioDAO.remove(this.usuario);
		}
		this.usuario = new Usuario();
	}

	@Test
	public void deveInserirUsuario() {
		this.setUsuario(this.usuario);
		this.usuarioDAO.adiciona(this.usuario);
		Assert.assertNotNull(this.usuario.getId());
		if (this.excluiUsuario == true) {
			this.excluirUsuario();
		}
	}

	@Test
	public void deveAlterarEmaildoUsuario() {
		this.excluiUsuario = false;
		this.deveInserirUsuario();
		this.usuario.setEmail("teste@teste.com");
		this.usuarioDAO.altera(this.usuario);
		Assert.assertEquals("teste@teste.com", this.usuario.getEmail());
		this.excluirUsuario();
	}

	@Test
	public void deveExcluirUsuario() {
		this.excluiUsuario = false;
		this.deveInserirUsuario();
		this.usuarioDAO.remove(this.usuario);
		Assert.assertNull(this.usuarioDAO.buscaString(this.nomeUsuario, "nome"));
	}

	@Test
	public void deveBuscarIdUsario() {
		this.excluiUsuario = false;
		this.deveInserirUsuario();
		Assert.assertNotNull(this.usuarioDAO.buscaId(this.usuario.getId()));
		this.excluirUsuario();
	}

	@Test
	public void deveBuscarStringNomeUsuario() {
		this.excluiUsuario = false;
		this.deveInserirUsuario();
		Assert.assertNotNull(this.usuarioDAO.buscaString(this.nomeUsuario, "nome"));
		this.excluirUsuario();
	}

	@Test
	public void deveListarUsuarios() {
		this.excluiUsuario = false;
		this.deveInserirUsuario();
		Assert.assertNotEquals(0, this.usuarioDAO.lista().size());
		this.excluirUsuario();
	}

}
