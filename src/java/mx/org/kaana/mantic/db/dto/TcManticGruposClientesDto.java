package mx.org.kaana.mantic.db.dto;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Lob;
import javax.persistence.Table;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/10/2016
 *@time 11:58:22 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Entity
@Table(name="tc_mantic_grupos_clientes")
public class TcManticGruposClientesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_grupo")
  private Long idGrupo;
  @Column (name="id_cliente")
  private Long idCliente;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_grupo_cliente")
  private Long idGrupoCliente;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticGruposClientesDto() {
    this(new Long(-1L));
  }

  public TcManticGruposClientesDto(Long key) {
    this(null, null, new Long(-1L));
    setKey(key);
  }

  public TcManticGruposClientesDto(Long idGrupo, Long idCliente, Long idGrupoCliente) {
    setIdGrupo(idGrupo);
    setIdCliente(idCliente);
    setIdGrupoCliente(idGrupoCliente);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdGrupo(Long idGrupo) {
    this.idGrupo = idGrupo;
  }

  public Long getIdGrupo() {
    return idGrupo;
  }

  public void setIdCliente(Long idCliente) {
    this.idCliente = idCliente;
  }

  public Long getIdCliente() {
    return idCliente;
  }

  public void setIdGrupoCliente(Long idGrupoCliente) {
    this.idGrupoCliente = idGrupoCliente;
  }

  public Long getIdGrupoCliente() {
    return idGrupoCliente;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdGrupoCliente();
  }

  @Override
  public void setKey(Long key) {
  	this.idGrupoCliente = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdGrupo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCliente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdGrupoCliente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idGrupo", getIdGrupo());
		regresar.put("idCliente", getIdCliente());
		regresar.put("idGrupoCliente", getIdGrupoCliente());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdGrupo(), getIdCliente(), getIdGrupoCliente(), getRegistro()
    };
    return regresar;
  }

  @Override
  public Object toValue(String name) {
    return Methods.getValue(this, name);
  }

  @Override
  public String toAllKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("|");
    regresar.append("idGrupoCliente~");
    regresar.append(getIdGrupoCliente());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdGrupoCliente());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticGruposClientesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdGrupoCliente()!= null && getIdGrupoCliente()!=-1L;
  }

	@Override
	public int hashCode() {
		int hash=3;
		hash=19*hash+Objects.hashCode(this.idGrupo);
		hash=19*hash+Objects.hashCode(this.idCliente);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this==obj) {
			return true;
		}
		if (obj==null) {
			return false;
		}
		if (getClass()!=obj.getClass()) {
			return false;
		}
		final TcManticGruposClientesDto other=(TcManticGruposClientesDto) obj;
		if (!Objects.equals(this.idGrupo, other.idGrupo)) {
			return false;
		}
		if (!Objects.equals(this.idCliente, other.idCliente)) {
			return false;
		}
		return true;
	}

}


