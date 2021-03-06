package br.com.caelum.evento.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.Days;
import org.joda.time.LocalDate;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "titulo", "evento_id" }, name = "unq_titulo_evento") })
public class Palestra implements Cloneable, Serializable {

	private static final long serialVersionUID = 5814590454769778466L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "palestrante", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_palestrante"), nullable = false)
	private Usuario palestrante;

	@NotEmpty(message = "T�tulo da Palestra � obrigat�rio.")
	@Size(min = 5, max = 100, message = "Tamanho inv�lido para o campo Palestra (5 at� 100).")
	@Column(length = 100, nullable = false)
	private String titulo;

	@NotEmpty(message = "Descri��o da Palestra � obrigat�rio.")
	@Size(min = 5, max = 200, message = "Tamanho inv�lido para o campo Descri��o (5 at� 200).")
	@Column(length = 200, nullable = false)
	private String descricao;

	@ManyToOne
	@JoinColumn(name = "evento_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_evento"), nullable = false)
	private Evento evento;

	@OneToMany(mappedBy = "palestra_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<Votacao> votacao;

	@OneToMany(mappedBy = "palestra")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<Comentario> comentario;

	public Palestra() {
		super();
	}

	public Palestra(Usuario palestrante, String titulo, String descricao, Evento evento) {
		super();
		this.palestrante = palestrante;
		this.titulo = titulo;
		this.descricao = descricao;
		this.evento = evento;
	}

	@Override
	public Palestra clone() throws CloneNotSupportedException {
		return (Palestra) super.clone();
	}

	public boolean podeSubmeterPalestra() {
		if (!this.getEvento().isPermiteSubmissao()) {
			return false;
		}
		LocalDate dataAtual = new LocalDate();
		LocalDate dataEvento = this.getEvento().getData();
		if (Days.daysBetween(dataAtual, dataEvento).getDays() <= 0) {
			return false;
		}
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getPalestrante() {
		return palestrante;
	}

	public void setPalestrante(Usuario palestrante) {
		this.palestrante = palestrante;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public List<Votacao> getVotacao() {
		return votacao;
	}

	public void setVotacao(List<Votacao> votacao) {
		this.votacao = votacao;
	}

	public List<Comentario> getComentario() {
		return comentario;
	}

	public void setComentario(List<Comentario> comentario) {
		this.comentario = comentario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comentario == null) ? 0 : comentario.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((evento == null) ? 0 : evento.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((palestrante == null) ? 0 : palestrante.hashCode());
		result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
		result = prime * result + ((votacao == null) ? 0 : votacao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Palestra other = (Palestra) obj;
		if (comentario == null) {
			if (other.comentario != null)
				return false;
		} else if (!comentario.equals(other.comentario))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (evento == null) {
			if (other.evento != null)
				return false;
		} else if (!evento.equals(other.evento))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (palestrante == null) {
			if (other.palestrante != null)
				return false;
		} else if (!palestrante.equals(other.palestrante))
			return false;
		if (titulo == null) {
			if (other.titulo != null)
				return false;
		} else if (!titulo.equals(other.titulo))
			return false;
		if (votacao == null) {
			if (other.votacao != null)
				return false;
		} else if (!votacao.equals(other.votacao))
			return false;
		return true;
	}

}
