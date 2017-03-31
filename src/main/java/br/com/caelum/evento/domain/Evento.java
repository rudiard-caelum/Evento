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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = "nome", name = "unq_nome") })
public class Evento implements Serializable {

	private static final long serialVersionUID = 6883422926597161974L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = "Nome do evento é obrigatório.")
	@Size(min = 5, max = 100, message = "Tamanho inválido para o campo (5 até 100).")
	@Column(length = 100, nullable = false)
	private String nome;

	@NotEmpty(message = "Descrição do evento é obrigatório.")
	@Size(min = 5, max = 200, message = "Tamanho inválido para o campo (5 até 200).")
	@Column(length = 200, nullable = false)
	private String descricao;

	@NotEmpty(message = "Site do evento é obrigatório.")
	@Column(length = 200, nullable = false)
	private String site;

	@ManyToOne
	@JoinColumn(name = "usuario_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_usuario"), nullable = false)
	private Usuario usuario;

	@NotEmpty(message = "Local do evento é obrigatório.")
	@Size(min = 5, max = 200, message = "Tamanho inválido para o campo (5 até 200).")
	@Column(length = 200, nullable = false)
	private String local;

	@Column(length = 200, nullable = true)
	private String logo;

	@NotNull(message = "Data do evento é obrigatória.")
	@Column(nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate data = new LocalDate();

	private boolean permiteSubmissao = true;

	@OneToMany(mappedBy = "evento")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<Palestra> palestras;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public boolean isPermiteSubmissao() {
		return permiteSubmissao;
	}

	public void setPermiteSubmissao(boolean permiteSubmissao) {
		this.permiteSubmissao = permiteSubmissao;
	}

	public List<Palestra> getPalestras() {
		return palestras;
	}

	public void setPalestras(List<Palestra> palestras) {
		this.palestras = palestras;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((local == null) ? 0 : local.hashCode());
		result = prime * result + ((logo == null) ? 0 : logo.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((palestras == null) ? 0 : palestras.hashCode());
		result = prime * result + (permiteSubmissao ? 1231 : 1237);
		result = prime * result + ((site == null) ? 0 : site.hashCode());
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
		Evento other = (Evento) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (local == null) {
			if (other.local != null)
				return false;
		} else if (!local.equals(other.local))
			return false;
		if (logo == null) {
			if (other.logo != null)
				return false;
		} else if (!logo.equals(other.logo))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (palestras == null) {
			if (other.palestras != null)
				return false;
		} else if (!palestras.equals(other.palestras))
			return false;
		if (permiteSubmissao != other.permiteSubmissao)
			return false;
		if (site == null) {
			if (other.site != null)
				return false;
		} else if (!site.equals(other.site))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}

}
