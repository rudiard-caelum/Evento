package br.com.caelum.evento.dao;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import br.com.caelum.evento.domain.Usuario;

public class UsuarioDAOTest {

	private String nomeUsuario;
	private String senhaUsuario = "123456";
	private String emailUsuario = "teste@teste.com";
	private boolean excluiUsuario = true;
	private Usuario usuario = new Usuario();
	private UsuarioDAO usuarioDAO = new UsuarioDAO();

	private void setUsuario(Usuario usr) {
		Random rdm = new Random();
		Integer inteiro = rdm.nextInt();
		this.nomeUsuario = "TESTE_" + inteiro.toString();
		usr.setNome(this.nomeUsuario);
		usr.setEmail(this.emailUsuario);
		usr.setSenha(this.senhaUsuario);
	}

	private void excluirUsuario() {
		this.usuario = (Usuario) this.usuarioDAO.buscaString(this.nomeUsuario, "nome");
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
	public void deveAlterarSenha() {
		this.excluiUsuario = false;
		this.deveInserirUsuario();
		this.usuario.setSenha("123");
		this.usuarioDAO.altera(this.usuario);
		Assert.assertEquals("123", this.usuario.getSenha());
		this.excluirUsuario();
	}

	@Test
	public void deveExcluirUsuario() {
		this.excluiUsuario = false;
		this.deveInserirUsuario();
		this.usuarioDAO.remove(this.usuario);
		this.usuario = (Usuario) this.usuarioDAO.buscaString(this.nomeUsuario, "nome");
		Assert.assertNull(this.usuario);
	}

	@Test
	public void deveBuscarId() {
		this.excluiUsuario = false;
		this.deveInserirUsuario();
		Assert.assertNotNull(this.usuarioDAO.buscaId(this.usuario.getId()));
		this.excluirUsuario();
	}

	@Test
	public void deveBuscarString() {
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
