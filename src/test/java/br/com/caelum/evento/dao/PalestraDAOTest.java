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

public class PalestraDAOTest {

	private static EntityManagerFactory emf;
	private EntityManager manager;

	private Evento evento;
	private Usuario palestrante;
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

	private void setPalestra(Palestra plt) {
		this.palestrante = new Usuario("TESTE_" + randomNumber(), "teste@teste.com", "123");
		this.evento = new Evento("TESTE_" + randomNumber(), "DESCRICAO DO EVENTO", "www.caelum.com.br",
				this.palestrante, "LOCAL", "LOGO", new LocalDate(), true);
		this.palestra = new Palestra(this.palestrante, "TESTE_" + randomNumber(), "DESCRICAO DA PALESTRA", this.evento);
	}

	private boolean inserirPalestra() {
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
		return true;
	}

	private boolean inserirVotacao(int qtdeVotoPositivo, int qtdeVotoNegativo) {
		try {
			this.votacao = new Votacao(this.palestrante, this.palestra, VotacaoEnum.POSITIVO, qtdeVotoPositivo);
			this.votacaoDAO.adiciona(this.votacao);
			Usuario usuario2 = this.palestrante.clone();
			usuario2.setId(null);
			usuario2.setNome("TESTE_" + this.randomNumber());
			usuarioDAO.adiciona(usuario2);
			this.votacao = new Votacao(usuario2, this.palestra, VotacaoEnum.NEGATIVO, qtdeVotoNegativo);
			this.votacaoDAO.adiciona(this.votacao);
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	@Test
	public void deveInserirPalestra() {
		this.setPalestra(this.palestra);
		Assert.assertTrue(this.inserirPalestra());
	}

	@Test(expected = PersistenceException.class)
	public void naoDeveInserirDuasPalestrasIguaisnoMesmoEvento() throws CloneNotSupportedException {
		this.deveInserirPalestra();
		Palestra palestra2 = palestra.clone();
		try {
			this.palestraDAO.adiciona(palestra2);
		} catch (PersistenceException ex) {
			throw new PersistenceException(ex);
		}
	}

	@Test
	public void deveAlterarDescricaoPalestra() {
		this.deveInserirPalestra();
		this.palestra.setDescricao("NOVA DESCRICAO");
		this.palestraDAO.altera(this.palestra);
		Assert.assertEquals("NOVA DESCRICAO", this.palestra.getDescricao());
	}

	@Test
	public void deveExcluirPalestra() {
		this.deveInserirPalestra();
		this.palestraDAO.remove(this.palestra);
		this.palestra = (Palestra) this.palestraDAO.buscaString(this.palestra.getTitulo(), "titulo");
		Assert.assertNull(this.palestra);
	}

	@Test
	public void deveBuscarIdPalestra() {
		this.deveInserirPalestra();
		Assert.assertNotNull(this.palestraDAO.buscaId(this.palestra.getId()));
	}

	@Test
	public void deveBuscarStringTituloPalestra() {
		this.deveInserirPalestra();
		Assert.assertNotNull(this.palestraDAO.buscaString(this.palestra.getTitulo(), "titulo"));
	}

	@Test
	public void deveListarPalestras() {
		this.deveInserirPalestra();
		Assert.assertNotEquals(0, this.palestraDAO.lista().size());
	}

	@Test
	public void podeSubmeterPalestraQualquerDiaAntesDoEvento() {
		this.setPalestra(this.palestra);
		this.evento.setData(this.evento.getData().plusDays(10));
		Assert.assertTrue(this.palestra.podeSubmeterPalestra());
	}

	@Test
	public void podeSubmeterPalestraAteDiaAnteriorDoEvento() {
		this.setPalestra(this.palestra);
		this.evento.setData(this.evento.getData().plusDays(1));
		Assert.assertTrue(this.palestra.podeSubmeterPalestra());
	}

	@Test
	public void naoPodeSubmeterPalestraNoMesmoDiaDoEvento() {
		this.setPalestra(this.palestra);
		Assert.assertFalse(this.palestra.podeSubmeterPalestra());
	}

	@Test
	public void naoPodeSubmeterPalestraAposEvento() {
		this.setPalestra(this.palestra);
		this.evento.setData(this.evento.getData().minusDays(1));
		Assert.assertFalse(this.palestra.podeSubmeterPalestra());
	}

	@Test
	public void naoPodeSubmeterPalestraParaEventoFechado() {
		this.setPalestra(this.palestra);
		this.evento.setPermiteSubmissao(false);
		this.evento.setData(this.evento.getData().plusDays(1));
		Assert.assertFalse(this.palestra.podeSubmeterPalestra());
	}

	@Test
	public void deveEncontrarPalestraDoUsuario() {
		this.deveInserirPalestra();
		Assert.assertNotNull(palestraDAO.getPalestra(this.evento, this.palestra.getTitulo()));
	}

	@Test
	public void deveEncontrarPalestraDoEvento() {
		this.deveInserirPalestra();
		Assert.assertNotNull(palestraDAO.getPalestraEvento(this.evento));
	}

	@Test
	public void deveRetornarRankingNaoAvaliada() throws CloneNotSupportedException {
		this.deveInserirPalestra();
		Assert.assertEquals("NAO AVALIADA", this.palestraDAO.rankingVotacaoPorPalestra(this.palestra));

	}

	@Test
	public void deveRetornarRankingPolemica() throws CloneNotSupportedException {
		this.deveInserirPalestra();
		this.inserirVotacao(52, 48);
		Assert.assertEquals("POLEMICA", this.palestraDAO.rankingVotacaoPorPalestra(this.palestra));
	}

	@Test
	public void deveRetornarRankingAprovadaComMaioriaPositivo() throws CloneNotSupportedException {
		this.deveInserirPalestra();
		this.inserirVotacao(70, 40);
		Assert.assertEquals("APROVADA", this.palestraDAO.rankingVotacaoPorPalestra(this.palestra));
	}

}
