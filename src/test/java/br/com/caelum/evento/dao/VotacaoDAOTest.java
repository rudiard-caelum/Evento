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

import br.com.caelum.evento.domain.Evento;
import br.com.caelum.evento.domain.Palestra;
import br.com.caelum.evento.domain.Usuario;
import br.com.caelum.evento.domain.Votacao;
import br.com.caelum.evento.domain.VotacaoEnum;

public class VotacaoDAOTest {

	private static EntityManagerFactory emf;
	private EntityManager manager;

	private Usuario palestrante;
	private Evento evento;
	private Palestra palestra;
	private Votacao votacao;

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

	private String randomNumber() {
		Random rdm = new Random();
		Integer inteiro = rdm.nextInt();
		return inteiro.toString();
	}

	private void setVotacao(VotacaoEnum tipoVoto, Integer voto) {
		this.palestrante = new Usuario("TESTE_" + randomNumber(), "teste@teste.com", "123");
		this.evento = new Evento(this.palestrante.getNome(), "DESCRICAO DO EVENTO", "www.caelum.com.br",
				this.palestrante, "LOCAL", "LOGO", new LocalDate(), true);
		this.palestra = new Palestra(this.palestrante, "TITULO DA PALESTRA", "DESCRICAO DA PALESTRA", this.evento);
		this.votacao = new Votacao(this.palestrante, this.palestra, tipoVoto, voto);
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
			return false;
		}
		try {
			this.palestraDAO.adiciona(this.palestra);
		} catch (Exception ex) {
			return false;
		}
		try {
			this.votacaoDAO.adiciona(this.votacao);
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	@Test
	public void deveInserirVotacao() {
		this.setVotacao(VotacaoEnum.POSITIVO, 1);
		Assert.assertTrue(this.inserirVotacao());
	}

	@Test
	public void deveAlterarAvaliacaoParaNegativo() {
		this.deveInserirVotacao();
		this.votacao.setTipoVoto(VotacaoEnum.NEGATIVO);
		this.votacaoDAO.altera(this.votacao);
		Assert.assertEquals(VotacaoEnum.NEGATIVO, this.votacao.getTipoVoto());
	}

	@Test
	public void deveExcluirVotacao() {
		this.deveInserirVotacao();
		this.votacaoDAO.remove(this.votacao);
		Assert.assertFalse(this.votacaoDAO.buscaVotoUsuario(this.palestrante, this.palestra));
	}

	@Test
	public void deveBuscarIdVotacao() {
		this.deveInserirVotacao();
		Assert.assertNotNull(this.votacaoDAO.buscaId(this.votacao.getId()));
	}

	@Test
	public void deveListarVotacao() {
		this.deveInserirVotacao();
		Assert.assertNotEquals(0, this.votacaoDAO.lista().size());
	}

	@Test
	public void deveListarAvaliacoesRealizadas() {
		this.deveInserirVotacao();
		Assert.assertNotEquals(0, this.votacaoDAO.listaAvaliacao(this.palestrante, "Realizada").size());
	}

	@Test
	public void deveListarAvaliacoesNaoRealizadas() {
		this.deveInserirVotacao();
		Usuario usuario = new Usuario("TESTE_" + randomNumber(), "teste@teste.com", "123");
		usuarioDAO.adiciona(usuario);
		Assert.assertNotEquals(0, this.votacaoDAO.listaAvaliacao(usuario, "NaoRealizada").size());
	}

	@Test
	public void deveVerificarSeUsuarioVotou() {
		this.deveInserirVotacao();
		Assert.assertTrue(this.votacaoDAO.buscaVotoUsuario(this.palestrante, this.palestra));
	}

	@Test
	public void deveEncontrarVotoDoUsuario() {
		this.deveInserirVotacao();
		Assert.assertNotNull(this.votacaoDAO.getVotacao(this.palestrante, this.palestra));
	}

	@Test(expected = PersistenceException.class)
	public void naoDeveInserirDuasAvaliacoesIguaisnaMesmaPalestra() throws CloneNotSupportedException {
		this.deveInserirVotacao();
		Votacao votacao2 = votacao.clone();
		try {
			this.votacaoDAO.adiciona(votacao2);
		} catch (PersistenceException ex) {
			throw new PersistenceException(ex);
		}
	}

	@Test
	public void deveSerDoisOPesoDoVotoParaUsuarioIgualAPalestrante() {
		this.deveInserirVotacao();
		Assert.assertEquals(new Integer(2), this.votacao.pesoVoto(this.votacao.getUsuario_id()));
	}

	@Test
	public void deveSerUmOPesoDoVotoParaUsuarioDiferenteDePalestrante() {
		this.deveInserirVotacao();
		Usuario usuarioDiferente = new Usuario();
		usuarioDiferente.setNome("TESTE");
		Assert.assertEquals(new Integer(1), this.votacao.pesoVoto(usuarioDiferente));
	}

}
