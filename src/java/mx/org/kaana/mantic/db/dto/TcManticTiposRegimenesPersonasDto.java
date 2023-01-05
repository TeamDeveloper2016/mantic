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
@Table(name="tc_mantic_tipos_regimenes_personas")
public class TcManticTiposRegimenesPersonasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="descripcion")
  private String descripcion;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_tipo_regimen_persona")
  private Long idTipoRegimenPersona;
  @Column (name="nombre")
  private String nombre;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticTiposRegimenesPersonasDto() {
    this(new Long(-1L));
  }

  public TcManticTiposRegimenesPersonasDto(Long key) {
    this(null, new Long(-1L), null);
    setKey(key);
  }

  public TcManticTiposRegimenesPersonasDto(String descripcion, Long idTipoRegimenPersona, String nombre) {
    setDescripcion(descripcion);
    setIdTipoRegimenPersona(idTipoRegimenPersona);
    setNombre(nombre);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setIdTipoRegimenPersona(Long idTipoRegimenPersona) {
    this.idTipoRegimenPersona = idTipoRegimenPersona;
  }

  public Long getIdTipoRegimenPersona() {
    return idTipoRegimenPersona;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
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
  	return getIdTipoRegimenPersona();
  }

  @Override
  public void setKey(Long key) {
  	this.idTipoRegimenPersona = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoRegimenPersona());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("descripcion", getDescripcion());
		regresar.put("idTipoRegimenPersona", getIdTipoRegimenPersona());
		regresar.put("nombre", getNombre());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getDescripcion(), getIdTipoRegimenPersona(), getNombre(), getRegistro()
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
    regresar.append("idTipoRegimenPersona~");
    regresar.append(getIdTipoRegimenPersona());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdTipoRegimenPersona());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticTiposRegimenesPersonasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdTipoRegimenPersona()!= null && getIdTipoRegimenPersona()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticTiposRegimenesPersonasDto other = (TcManticTiposRegimenesPersonasDto) obj;
    if (getIdTipoRegimenPersona() != other.idTipoRegimenPersona && (getIdTipoRegimenPersona() == null || !getIdTipoRegimenPersona().equals(other.idTipoRegimenPersona))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdTipoRegimenPersona() != null ? getIdTipoRegimenPersona().hashCode() : 0);
    return hash;
  }

}


