package br.com.caelum.evento.dao;

import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.caelum.evento.domain.Evento;
import br.com.caelum.evento.domain.Palestra;
import br.com.caelum.evento.domain.Usuario;
import br.com.caelum.evento.domain.Votacao;
import br.com.caelum.evento.domain.VotacaoEnum;

public class VotacaoDAOTest {

	private static EntityManagerFactory emf;
	private EntityManager manager;

	private boolean excluiVotacao = true;

	private Usuario palestrante = new Usuario();
	private Evento evento = new Evento();
	private Palestra palestra = new Palestra();
	private Votacao votacao = new Votacao();

	private UsuarioDAO usuarioDAO;
	private EventoDAO eventoDAO;
	private PalestraDAO palestraDAO;
	private VotacaoDAO votacaoDAO;

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
		this.votacaoDAO = new VotacaoDAO(manager);
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

	private void setVotacao(Votacao vt, VotacaoEnum tipoVoto, Integer voto) {
		Random rdm = new Random();
		Integer inteiro = rdm.nextInt();
		this.palestrante.setNome("TESTE_" + inteiro.toString());
		this.palestrante.setSenha("123");
		this.evento.setNome(this.palestrante.getNome());
		this.evento.setDescricao("DESCRICAO DO EVENTO");
		this.evento.setLocal("LOCAL DO EVENTO");
		this.evento.setLogo("LOGO DO EVENTO");
		this.evento.setSite("www.caelum.com.br");
		this.evento.setUsuario(this.palestrante);
		this.palestra.setPalestrante(this.palestrante);
		this.palestra.setTitulo(this.palestrante.getNome());
		this.palestra.setDescricao("DESCRICAO DA PALESTRA");
		this.palestra.setEvento(this.evento);
		vt.setPalestra_id(this.palestra);
		vt.setUsuario_id(this.palestrante);
		vt.setTipoVoto(tipoVoto);
		vt.setVoto(voto);
	}

	private boolean inserirVotacao() {
		try {
			this.usuarioDAO.adiciona(this.palestrante);
		} catch (Exception ex) {
			return false;
		}
		try {
			this.eventoDAO.adiciona(this.evento);
		} catch (Exception ex) {
			this.usuarioDAO.remove(this.palestrante);
			return false;
		}
		try {
			this.palestraDAO.adiciona(this.palestra);
		} catch (Exception ex) {
			this.eventoDAO.remove(evento);
			this.usuarioDAO.remove(this.palestrante);
		}
		try {
			this.votacaoDAO.adiciona(this.votacao);
		} catch (Exception ex) {
			this.eventoDAO.remove(evento);
			this.usuarioDAO.remove(this.palestrante);
			this.palestraDAO.remove(this.palestra);
		}
		return true;
	}

	private void excluirVotacao() {
		if (this.evento.getId() != null) {
			this.eventoDAO.remove(this.evento);
		}
		if (this.palestrante.getId() != null) {
			this.usuarioDAO.remove(this.palestrante);
		}
	}

	@Test
	public void deveInserirVotacao() {
		this.setVotacao(this.votacao, VotacaoEnum.POSITIVO, 1);
		Assert.assertTrue(this.inserirVotacao());
		if (this.excluiVotacao == true) {
			this.excluirVotacao();
		}
	}

	@Test
	public void deveAlterarAvaliacaoParaNegativo() {
		this.excluiVotacao = false;
		this.deveInserirVotacao();
		this.votacao.setTipoVoto(VotacaoEnum.NEGATIVO);
		this.votacaoDAO.altera(this.votacao);
		Assert.assertEquals(VotacaoEnum.NEGATIVO, this.votacao.getTipoVoto());
		this.excluirVotacao();
	}

	@Test
	public void deveExcluirVotacao() {
		this.excluiVotacao = false;
		this.deveInserirVotacao();
		this.votacaoDAO.remove(this.votacao);
		Assert.assertFalse(this.votacaoDAO.buscaVotoUsuario(this.palestrante, this.palestra));
		this.eventoDAO.remove(this.evento);
		this.evento = (Evento) this.eventoDAO.buscaString(this.palestrante.getNome(), "nome");
		Assert.assertNull(this.evento);
		Long usuarioId = this.palestrante.getId();
		this.usuarioDAO.remove(this.palestrante);
		Assert.assertNull((Usuario) this.usuarioDAO.buscaId(usuarioId));
	}

	@Test
	public void deveBuscarIdVotacao() {
		this.excluiVotacao = false;
		this.deveInserirVotacao();
		Assert.assertNotNull(this.votacaoDAO.buscaId(this.votacao.getId()));
		this.excluirVotacao();
	}

	@Test
	public void deveListarVotacao() {
		this.excluiVotacao = false;
		this.deveInserirVotacao();
		Assert.assertNotEquals(0, this.votacaoDAO.lista().size());
		this.excluirVotacao();
	}

	@Test(expected = PersistenceException.class)
	public void naoDeveInserirDuasAvaliacoesIguaisnaMesmaPalestra() throws CloneNotSupportedException {
		this.excluiVotacao = false;
		this.deveInserirVotacao();
		Votacao votacao2 = votacao.clone();
		try {
			this.votacaoDAO.adiciona(votacao2);
		} catch (PersistenceException ex) {
			throw new PersistenceException(ex);
		} finally {
			this.excluirVotacao();
		}
	}

	@Test
	public void deveSerDoisOPesoDoVotoParaUsuarioIgualAPalestrante() {
		this.excluiVotacao = false;
		this.deveInserirVotacao();
		Assert.assertEquals(new Integer(2), this.votacao.pesoVoto(this.votacao.getUsuario_id()));
		this.excluirVotacao();
	}

	@Test
	public void deveSerUmOPesoDoVotoParaUsuarioDiferenteDePalestrante() {
		this.excluiVotacao = false;
		this.deveInserirVotacao();
		Usuario usuarioDiferente = new Usuario();
		usuarioDiferente.setNome("TESTE");
		Assert.assertEquals(new Integer(1), this.votacao.pesoVoto(usuarioDiferente));
		this.excluirVotacao();
	}

}
