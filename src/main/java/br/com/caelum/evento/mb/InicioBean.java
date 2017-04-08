package br.com.caelum.evento.mb;

import java.io.Serializable;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.joda.time.LocalDate;
import org.omnifaces.util.Messages;

import br.com.caelum.evento.dao.ComentarioDAO;
import br.com.caelum.evento.dao.EventoDAO;
import br.com.caelum.evento.dao.PalestraDAO;
import br.com.caelum.evento.dao.UsuarioDAO;
import br.com.caelum.evento.dao.VotacaoDAO;
import br.com.caelum.evento.domain.Comentario;
import br.com.caelum.evento.domain.Evento;
import br.com.caelum.evento.domain.Palestra;
import br.com.caelum.evento.domain.Usuario;
import br.com.caelum.evento.domain.Votacao;
import br.com.caelum.evento.domain.VotacaoEnum;
import br.com.caelum.evento.tx.Transactional;

@Model
public class InicioBean implements Serializable {

	private static final long serialVersionUID = 1700403527847049967L;

	@Inject
	private EntityManager manager;

	@Inject
	private UsuarioDAO usuarioDAO;

	@Inject
	private EventoDAO eventoDAO;

	@Inject
	private PalestraDAO palestraDAO;

	@Inject
	private VotacaoDAO votacaoDAO;

	@Inject
	private ComentarioDAO comentarioDAO;

	private Usuario usuario = new Usuario();
	private Evento evento = new Evento();
	private Palestra palestra = new Palestra();
	private Votacao votacao = new Votacao();
	private Comentario comentario = new Comentario();

	public Usuario getUsuario() {
		return usuario;
	}

	@Transactional
	public void criarUsuarioAdmin() {
		if (this.usuario.getSenha().isEmpty() || this.usuario.getSenha() == null) {
			Messages.addGlobalError("Senha inválida.");
			return;
		}
		this.usuario.setNome("ADMIN");
		this.usuario.setEmail("admin@admin.com.br");
		Usuario usuarioAdmin = (Usuario) usuarioDAO.buscaString(this.usuario.getNome(), "nome");
		if (usuarioAdmin == null) {
			usuarioDAO.adiciona(this.usuario);
			Messages.addGlobalInfo("Usuário ADMIN criado com sucesso.");
		} else {
			Messages.addGlobalError("Usuário ADMIN não foi criado pois já existe no banco de dados.");
		}
	}

	public void excluirDados() {
		this.excluiDadosBanco();
		Messages.addGlobalInfo("Dados excluídos do banco com sucesso.");
	}

	public void preencheBanco() {
		this.excluiDadosBanco();
		try {
			this.manager.getTransaction().begin();
			for (int i = 1; i <= 110; i++) {
				usuario = new Usuario();
				usuario.setNome("ADMIN_TESTE_" + i);
				usuario.setEmail("admin" + i + "@admin.com.br");
				usuario.setSenha("123");
				usuarioDAO.adiciona(usuario);
			}
			this.manager.getTransaction().commit();
		} catch (Exception e) {
			this.manager.getTransaction().rollback();
			Messages.addGlobalError("Erro na criação de usuários.");
			e.printStackTrace();
			this.excluiDadosBanco();
			return;
		}
		try {
			this.manager.getTransaction().begin();
			for (int i = 1; i <= 4; i++) {
				usuario = new Usuario();
				usuario = (Usuario) usuarioDAO.buscaString("ADMIN_TESTE_" + i, "nome");
				evento = new Evento();
				evento.setNome("EVENTO TESTE " + i);
				evento.setDescricao("EVENTO DE TESTE " + i + " CRIADO AUTOMATICAMENTE.");
				evento.setLocal("SAO PAULO");
				evento.setLogo("LOGOTIPO DO EVENTO " + i);
				evento.setSite("www.caelum.com.br");
				evento.setData(new LocalDate().plusDays(i * 7));
				evento.setUsuario(usuario);
				if (i == 4) {
					evento.setPermiteSubmissao(false);
				}
				eventoDAO.adiciona(evento);
			}
			this.manager.getTransaction().commit();
		} catch (Exception e) {
			this.manager.getTransaction().rollback();
			Messages.addGlobalError("Erro na criação de eventos.");
			e.printStackTrace();
			this.excluiDadosBanco();
			return;
		}
		try {
			this.manager.getTransaction().begin();
			for (int e = 1; e <= 4; e++) {
				evento = new Evento();
				evento = (Evento) eventoDAO.buscaString("EVENTO TESTE " + e, "nome");
				for (int i = 1; i <= 5; i++) {
					usuario = new Usuario();
					usuario = (Usuario) usuarioDAO.buscaString("ADMIN_TESTE_" + i, "nome");
					palestra = new Palestra();
					palestra.setPalestrante(usuario);
					palestra.setTitulo("PALESTRA TESTE " + i);
					palestra.setDescricao("DESCRICAO DA PALESTRA TESTE " + i);
					palestra.setEvento(evento);
					palestraDAO.adiciona(palestra);
					for (int c = 1; c <= 5; c++) {
						usuario = new Usuario();
						usuario = (Usuario) usuarioDAO.buscaString("ADMIN_TESTE_" + c, "nome");
						comentario = new Comentario();
						comentario.setUsuario(usuario);
						comentario.setPalestra(palestra);
						comentario.setData(new LocalDate());
						comentario.setComentario("COMENTARIO DA PALESTRA " + i);
						comentarioDAO.adiciona(comentario);
					}
				}
			}
			this.manager.getTransaction().commit();
		} catch (Exception e) {
			Messages.addGlobalError("Erro na criação de palestras.");
			this.manager.getTransaction().rollback();
			e.printStackTrace();
			this.excluiDadosBanco();
			return;
		}
		if (!this.criaVotacao("EVENTO TESTE 1", "PALESTRA TESTE 1", 1, 60, VotacaoEnum.POSITIVO)) {
			return;
		}
		if (!this.criaVotacao("EVENTO TESTE 1", "PALESTRA TESTE 1", 61, 90, VotacaoEnum.NEGATIVO)) {
			return;
		}
		if (!this.criaVotacao("EVENTO TESTE 1", "PALESTRA TESTE 2", 1, 33, VotacaoEnum.POSITIVO)) {
			return;
		}
		if (!this.criaVotacao("EVENTO TESTE 1", "PALESTRA TESTE 2", 34, 60, VotacaoEnum.NEGATIVO)) {
			return;
		}
		if (!this.criaVotacao("EVENTO TESTE 1", "PALESTRA TESTE 3", 1, 70, VotacaoEnum.POSITIVO)) {
			return;
		}
		if (!this.criaVotacao("EVENTO TESTE 1", "PALESTRA TESTE 3", 71, 110, VotacaoEnum.NEGATIVO)) {
			return;
		}
		Messages.addGlobalInfo("Banco preenchido com sucesso.");
	}

	private void excluiDadosBanco() {
		try {
			this.manager.getTransaction().begin();
			String jpql = "delete from Evento where nome like 'EVENTO TESTE%'";
			Query query = manager.createQuery(jpql);
			query.executeUpdate();
			jpql = "delete from Usuario where nome like 'ADMIN_TESTE%'";
			query = manager.createQuery(jpql);
			query.executeUpdate();
			this.manager.getTransaction().commit();
		} catch (Exception e) {
			Messages.addGlobalError("Erro na exclusão de dados.");
			this.manager.getTransaction().rollback();
			e.printStackTrace();
		}
	}

	private boolean criaVotacao(String nomeEvento, String nomePalestra, int initVotos, int qtdeVotos,
			VotacaoEnum tipoVoto) {
		try {
			this.manager.getTransaction().begin();
			evento = new Evento();
			evento = (Evento) eventoDAO.buscaString(nomeEvento, "nome");
			palestra = new Palestra();
			palestra = palestraDAO.getPalestra(evento, nomePalestra);
			for (int v = initVotos; v <= qtdeVotos; v++) {
				usuario = new Usuario();
				usuario = (Usuario) usuarioDAO.buscaString("ADMIN_TESTE_" + v, "nome");
				votacao = new Votacao();
				votacao.setPalestra_id(palestra);
				votacao.setUsuario_id(usuario);
				votacao.setTipoVoto(tipoVoto);
				votacao.setVoto(votacao.pesoVoto(usuario));
				votacaoDAO.adiciona(votacao);
			}
			this.manager.getTransaction().commit();
			return true;
		} catch (Exception e) {
			Messages.addGlobalError("Erro na criação da votação para o evento: " + evento.getNome() + ", palestra: "
					+ palestra.getTitulo() + ", votacao: " + tipoVoto.toString());
			this.manager.getTransaction().rollback();
			e.printStackTrace();
			this.excluiDadosBanco();
			return false;
		}

	}

}
