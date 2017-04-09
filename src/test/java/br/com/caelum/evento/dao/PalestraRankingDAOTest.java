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
import br.com.caelum.evento.domain.Palestra;
import br.com.caelum.evento.domain.Usuario;
import br.com.caelum.evento.domain.Votacao;
import br.com.caelum.evento.domain.VotacaoEnum;

public class PalestraRankingDAOTest {

	private static EntityManagerFactory emf;
	private EntityManager manager;

	private Usuario palestrante;
	private Evento evento;
	private Evento eventoPrimeiro;
	private Palestra palestra;
	private Votacao votacao;

	private UsuarioDAO usuarioDAO;
	private EventoDAO eventoDAO;
	private PalestraDAO palestraDAO;
	private VotacaoDAO votacaoDAO;
	private PalestraRankingDAO palestraRankingDAO;

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
		this.palestraRankingDAO = new PalestraRankingDAO(manager);
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

	private void criaDados() throws CloneNotSupportedException {
		for (int e = 1; e <= 2; e++) {
			this.palestrante = new Usuario("TESTE_" + randomNumber(), "teste@teste.com", "123");
			this.usuarioDAO.adiciona(this.palestrante);
			this.evento = new Evento(this.palestrante.getNome(), "DESCRICAO DO EVENTO", "www.caelum.com.br",
					this.palestrante, "LOCAL", "LOGO", new LocalDate(), true);
			this.eventoDAO.adiciona(this.evento);
			for (int i = 1; i <= 5; i++) {
				this.palestra = new Palestra(this.palestrante, "TITULO DA PALESTRA " + randomNumber(),
						"DESCRICAO DA PALESTRA", this.evento);
				this.palestraDAO.adiciona(this.palestra);
				if (i == 3) {
					this.criaVotacao(60, VotacaoEnum.POSITIVO);
					this.criaVotacao(30, VotacaoEnum.NEGATIVO);
				}
				if (i == 2) {
					this.criaVotacao(33, VotacaoEnum.POSITIVO);
					this.criaVotacao(27, VotacaoEnum.NEGATIVO);
				}
				if (i == 1) {
					this.criaVotacao(70, VotacaoEnum.POSITIVO);
					this.criaVotacao(40, VotacaoEnum.NEGATIVO);
				}
			}
			if (e == 1) {
				this.eventoPrimeiro = this.evento.clone();
			}
		}
	}

	private void criaVotacao(int qtdeVotos, VotacaoEnum tipoVoto) {
		this.palestrante = new Usuario("TESTE_" + randomNumber(), "teste@teste.com", "123");
		this.usuarioDAO.adiciona(this.palestrante);
		this.votacao = new Votacao(this.palestrante, this.palestra, tipoVoto, qtdeVotos);
		this.votacaoDAO.adiciona(this.votacao);
	}

	@Test
	public void deveListarPalestrasRankeadas() throws CloneNotSupportedException {
		this.criaDados();
		Assert.assertNotNull(this.palestraRankingDAO.lista());
	}

	@Test
	public void deveListarPalestrasRankeadasPorEvento() throws CloneNotSupportedException {
		this.criaDados();
		Assert.assertNotNull(this.palestraRankingDAO.listaPorEvento(this.eventoPrimeiro));
	}

}
