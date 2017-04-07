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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "usuario_id", "palestra_id" }, name = "unq_usuario_comentario"))
@NamedQueries({
		@NamedQuery(name = "PalestraComentario.NaoRealizado", query = "select p from Palestra p where not exists(select 1 from Comentario c where c.palestra = p.id and c.usuario = :pUsuario)"),
		@NamedQuery(name = "PalestraComentario.Realizado", query = "select p from Palestra p where exists(select 1 from Comentario c where c.palestra = p.id and c.usuario = :pUsuario)") })
public class Comentario implements Cloneable, Serializable {

	private static final long serialVersionUID = 4783446426159758729L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate data = new LocalDate();

	@ManyToOne
	@JoinColumn(name = "usuario_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_usuario_comentario"), nullable = false)
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "palestra_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_palestra_comentario"), nullable = false)
	private Palestra palestra;

	@NotEmpty(message = "Comentário da Palestra é obrigatório.")
	@Size(min = 5, max = 255, message = "Tamanho inválido para o campo (5 até 255).")
	@Column(length = 255, nullable = false)
	private String comentario;

	public Comentario() {
		super();
	}

	public Comentario(LocalDate data, Usuario usuario, Palestra palestra, String comentario) {
		super();
		this.data = data;
		this.usuario = usuario;
		this.palestra = palestra;
		this.comentario = comentario;
	}

	@Override
	public Comentario clone() throws CloneNotSupportedException {
		return (Comentario) super.clone();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Palestra getPalestra() {
		return palestra;
	}

	public void setPalestra(Palestra palestra) {
		this.palestra = palestra;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comentario == null) ? 0 : comentario.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((palestra == null) ? 0 : palestra.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
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
		Comentario other = (Comentario) obj;
		if (comentario == null) {
			if (other.comentario != null)
				return false;
		} else if (!comentario.equals(other.comentario))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (palestra == null) {
			if (other.palestra != null)
				return false;
		} else if (!palestra.equals(other.palestra))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}

}
