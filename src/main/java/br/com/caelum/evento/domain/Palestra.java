package br.com.caelum.evento.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.joda.time.Days;
import org.joda.time.LocalDate;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = "titulo", name = "unq_titulo"),
		@UniqueConstraint(columnNames = { "titulo", "evento_id" }, name = "unq_titulo_evento") })
public class Palestra implements Cloneable, Serializable {

	private static final long serialVersionUID = -4351930299965095496L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "palestrante", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_palestrante"), nullable = false)
	private Usuario palestrante;

	@Column(length = 100, nullable = false)
	private String titulo;

	@Column(length = 200, nullable = false)
	private String descricao;

	@ManyToOne
	@JoinColumn(name = "evento_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_evento"), nullable = false)
	private Evento evento;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((evento == null) ? 0 : evento.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((palestrante == null) ? 0 : palestrante.hashCode());
		result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
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
		return true;
	}

}
