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

public class PalestraDAOTest {

	private static EntityManagerFactory emf;
	private EntityManager manager;

	private boolean excluiPalestra = true;

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

	private void setPalestra(Palestra plt) {
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
		plt.setPalestrante(this.palestrante);
		plt.setTitulo(this.palestrante.getNome());
		plt.setDescricao("DESCRICAO DA PALESTRA");
		plt.setEvento(this.evento);
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
			this.usuarioDAO.remove(this.palestrante);
			return false;
		}
		try {
			this.palestraDAO.adiciona(this.palestra);
		} catch (Exception ex) {
			this.eventoDAO.remove(evento);
			this.usuarioDAO.remove(this.palestrante);
			return false;
		}
		return true;
	}

	private boolean inserirVotacao(int qtdeVotoPositivo, int qtdeVotoNegativo) {
		try {
			this.evento.setData(this.evento.getData().plusDays(10));
			this.eventoDAO.altera(evento);
			this.votacao.setPalestra_id(this.palestra);
			this.votacao.setUsuario_id(this.palestrante);
			this.votacao.setTipoVoto(VotacaoEnum.POSITIVO);
			this.votacao.setVoto(qtdeVotoPositivo);
			this.votacaoDAO.adiciona(this.votacao);

			Random rdm = new Random();
			Integer inteiro = rdm.nextInt();
			Usuario usuario2 = this.palestrante.clone();
			usuario2.setId(null);
			usuario2.setNome("TESTE_" + inteiro.toString());
			this.usuarioDAO.adiciona(usuario2);

			this.votacao = new Votacao();
			this.votacao.setPalestra_id(this.palestra);
			this.votacao.setUsuario_id(usuario2);
			this.votacao.setTipoVoto(VotacaoEnum.NEGATIVO);
			this.votacao.setVoto(qtdeVotoNegativo);
			this.votacaoDAO.adiciona(this.votacao);
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	private void excluirPalestra() {
		if (this.evento.getId() != null) {
			this.eventoDAO.remove(this.evento);
		}
		if (this.palestrante.getId() != null) {
			this.usuarioDAO.remove(this.palestrante);
		}
	}

	@Test
	public void deveInserirPalestra() {
		this.setPalestra(this.palestra);
		Assert.assertTrue(this.inserirPalestra());
		if (this.excluiPalestra == true) {
			this.excluirPalestra();
		}
	}

	@Test
	public void deveAlterarDescricaoPalestra() {
		this.excluiPalestra = false;
		this.deveInserirPalestra();
		this.palestra.setDescricao("NOVA DESCRICAO");
		this.palestraDAO.altera(this.palestra);
		Assert.assertEquals("NOVA DESCRICAO", this.palestra.getDescricao());
		this.excluirPalestra();
	}

	@Test
	public void deveExcluirPalestra() {
		this.excluiPalestra = false;
		this.deveInserirPalestra();
		this.palestraDAO.remove(this.palestra);
		this.palestra = (Palestra) this.palestraDAO.buscaString(this.palestrante.getNome(), "titulo");
		Assert.assertNull(this.palestra);
		this.eventoDAO.remove(this.evento);
		this.evento = (Evento) this.eventoDAO.buscaString(this.palestrante.getNome(), "nome");
		Assert.assertNull(this.evento);
		Long usuarioId = this.palestrante.getId();
		this.usuarioDAO.remove(this.palestrante);
		Assert.assertNull((Usuario) this.usuarioDAO.buscaId(usuarioId));
	}

	@Test
	public void deveBuscarIdPalestra() {
		this.excluiPalestra = false;
		this.deveInserirPalestra();
		Assert.assertNotNull(this.palestraDAO.buscaId(this.palestra.getId()));
		this.excluirPalestra();
	}

	@Test
	public void deveBuscarStringTituloPalestra() {
		this.excluiPalestra = false;
		this.deveInserirPalestra();
		Assert.assertNotNull(this.palestraDAO.buscaString(this.palestra.getTitulo(), "titulo"));
		this.excluirPalestra();
	}

	@Test
	public void deveListarPalestras() {
		this.excluiPalestra = false;
		this.deveInserirPalestra();
		Assert.assertNotEquals(0, this.palestraDAO.lista().size());
		this.excluirPalestra();
	}

	@Test(expected = PersistenceException.class)
	public void naoDeveInserirDuasPalestrasIguaisnoMesmoEvento() throws CloneNotSupportedException {
		this.excluiPalestra = false;
		this.deveInserirPalestra();
		Palestra palestra2 = palestra.clone();
		try {
			this.palestraDAO.adiciona(palestra2);
		} catch (PersistenceException ex) {
			throw new PersistenceException(ex);
		} finally {
			this.excluirPalestra();
		}
	}

	@Test
	public void podeSubmeterPalestraQualquerDiaAntesDoEvento() {
		this.setPalestra(this.palestra);
		this.evento.setData(this.evento.getData().plusDays(10));
		Assert.assertTrue(this.palestra.podeSubmeterPalestra());
		this.excluirPalestra();
	}

	@Test
	public void podeSubmeterPalestraAteDiaAnteriorDoEvento() {
		this.setPalestra(this.palestra);
		this.evento.setData(this.evento.getData().plusDays(1));
		Assert.assertTrue(this.palestra.podeSubmeterPalestra());
		this.excluirPalestra();
	}

	@Test
	public void naoPodeSubmeterPalestraNoMesmoDiaDoEvento() {
		this.setPalestra(this.palestra);
		Assert.assertFalse(this.palestra.podeSubmeterPalestra());
		this.excluirPalestra();
	}

	@Test
	public void naoPodeSubmeterPalestraAposEvento() {
		this.setPalestra(this.palestra);
		this.evento.setData(this.evento.getData().minusDays(1));
		Assert.assertFalse(this.palestra.podeSubmeterPalestra());
		this.excluirPalestra();
	}

	@Test
	public void naoPodeSubmeterPalestraParaEventoFechado() {
		this.setPalestra(this.palestra);
		this.evento.setPermiteSubmissao(false);
		this.evento.setData(this.evento.getData().plusDays(1));
		Assert.assertFalse(this.palestra.podeSubmeterPalestra());
		this.excluirPalestra();
	}

	@Test
	public void deveRetornarRankingNaoAvaliada() throws CloneNotSupportedException {
		this.excluiPalestra = false;
		this.deveInserirPalestra();
		this.evento.setData(this.evento.getData().plusDays(10));
		this.eventoDAO.altera(evento);
		Assert.assertEquals("NAO AVALIADA", this.palestraDAO.rankingVotacaoPorPalestra(this.palestra));
		this.excluirPalestra();

	}

	@Test
	public void deveRetornarRankingPolemica() throws CloneNotSupportedException {
		this.excluiPalestra = false;
		this.deveInserirPalestra();
		this.evento.setData(this.evento.getData().plusDays(10));
		this.eventoDAO.altera(evento);
		this.inserirVotacao(52, 48);
		Assert.assertEquals("POLEMICA", this.palestraDAO.rankingVotacaoPorPalestra(this.palestra));
		this.excluirPalestra();
	}

	@Test
	public void deveRetornarRankingAprovadaComMaioriaPositivo() throws CloneNotSupportedException {
		this.excluiPalestra = false;
		this.deveInserirPalestra();
		this.evento.setData(this.evento.getData().plusDays(10));
		this.eventoDAO.altera(evento);
		this.inserirVotacao(70, 40);
		Assert.assertEquals("APROVADA", this.palestraDAO.rankingVotacaoPorPalestra(this.palestra));
		this.excluirPalestra();

	}

}
