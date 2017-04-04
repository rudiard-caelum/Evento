package br.com.caelum.evento.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "usuario_id", "palestra_id" }, name = "unq_usuario_voto"))
@NamedQueries({
		@NamedQuery(name = "Votacao.NaoRealizada", query = "select p from Palestra p where not exists(select 1 from Votacao v where v.palestra_id = p.id and usuario_id = :pUsuarioId)"),
		@NamedQuery(name = "Votacao.Realizada", query = "select p from Palestra p where exists(select 1 from Votacao v where v.palestra_id = p.id and v.usuario_id = :pUsuarioId)") })
public class Votacao implements Cloneable, Serializable {

	private static final long serialVersionUID = -2504854125515583998L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "usuario_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_usuario_voto"), nullable = false)
	private Usuario usuario_id;

	@ManyToOne
	@JoinColumn(name = "palestra_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_palestra_voto"), nullable = false)
	private Palestra palestra_id;

	@Enumerated(EnumType.STRING)
	private VotacaoEnum tipoVoto;

	private Integer voto = 0;

	public Votacao() {
		super();
	}

	public Votacao(Usuario usuario_id, Palestra palestra_id, VotacaoEnum tipoVoto, Integer voto) {
		super();
		this.usuario_id = usuario_id;
		this.palestra_id = palestra_id;
		this.tipoVoto = tipoVoto;
		this.voto = voto;
	}

	@Override
	public Votacao clone() throws CloneNotSupportedException {
		return (Votacao) super.clone();
	}

	public Integer pesoVoto(Usuario usuario) {
		return (usuario.getId() == this.getPalestra_id().getPalestrante().getId() ? 2 : 1);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario_id() {
		return usuario_id;
	}

	public void setUsuario_id(Usuario usuario_id) {
		this.usuario_id = usuario_id;
	}

	public Palestra getPalestra_id() {
		return palestra_id;
	}

	public void setPalestra_id(Palestra palestra_id) {
		this.palestra_id = palestra_id;
	}

	public VotacaoEnum getTipoVoto() {
		return tipoVoto;
	}

	public void setTipoVoto(VotacaoEnum tipoVoto) {
		this.tipoVoto = tipoVoto;
	}

	public Integer getVoto() {
		return voto;
	}

	public void setVoto(Integer voto) {
		this.voto = voto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((palestra_id == null) ? 0 : palestra_id.hashCode());
		result = prime * result + ((tipoVoto == null) ? 0 : tipoVoto.hashCode());
		result = prime * result + ((usuario_id == null) ? 0 : usuario_id.hashCode());
		result = prime * result + ((voto == null) ? 0 : voto.hashCode());
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
		Votacao other = (Votacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (palestra_id == null) {
			if (other.palestra_id != null)
				return false;
		} else if (!palestra_id.equals(other.palestra_id))
			return false;
		if (tipoVoto != other.tipoVoto)
			return false;
		if (usuario_id == null) {
			if (other.usuario_id != null)
				return false;
		} else if (!usuario_id.equals(other.usuario_id))
			return false;
		if (voto == null) {
			if (other.voto != null)
				return false;
		} else if (!voto.equals(other.voto))
			return false;
		return true;
	}

}
