package mx.org.kaana.mantic.db.dto;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
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
@Table(name="tc_mantic_unidades_medidas")
public class TcManticUnidadesMedidasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="clave")
  private String clave;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_unidad_medida")
  private Long idUnidadMedida;
  @Column (name="nombre")
  private String nombre;

  public TcManticUnidadesMedidasDto() {
    this(new Long(-1L));
  }

  public TcManticUnidadesMedidasDto(Long key) {
    this(null, new Long(-1L), null);
    setKey(key);
  }

  public TcManticUnidadesMedidasDto(String clave, Long idUnidadMedida, String nombre) {
    setClave(clave);
    setIdUnidadMedida(idUnidadMedida);
    setNombre(nombre);
  }
	
  public void setClave(String clave) {
    this.clave = clave;
  }

  public String getClave() {
    return clave;
  }

  public void setIdUnidadMedida(Long idUnidadMedida) {
    this.idUnidadMedida = idUnidadMedida;
  }

  public Long getIdUnidadMedida() {
    return idUnidadMedida;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdUnidadMedida();
  }

  @Override
  public void setKey(Long key) {
  	this.idUnidadMedida = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getClave());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUnidadMedida());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("clave", getClave());
		regresar.put("idUnidadMedida", getIdUnidadMedida());
		regresar.put("nombre", getNombre());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getClave(), getIdUnidadMedida(), getNombre()
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
    regresar.append("idUnidadMedida~");
    regresar.append(getIdUnidadMedida());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdUnidadMedida());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticUnidadesMedidasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdUnidadMedida()!= null && getIdUnidadMedida()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticUnidadesMedidasDto other = (TcManticUnidadesMedidasDto) obj;
    if (getIdUnidadMedida() != other.idUnidadMedida && (getIdUnidadMedida() == null || !getIdUnidadMedida().equals(other.idUnidadMedida))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdUnidadMedida() != null ? getIdUnidadMedida().hashCode() : 0);
    return hash;
  }

}


