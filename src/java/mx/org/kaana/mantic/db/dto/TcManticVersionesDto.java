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
@Table(name="tc_mantic_versiones")
public class TcManticVersionesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_activa")
  private Long idActiva;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="observaciones")
  private String observaciones;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_version")
  private Long idVersion;
  @Column (name="nombre")
  private String nombre;
  @Column (name="version")
  private String version;
  @Column (name="vigencia")
  private Timestamp vigencia;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticVersionesDto() {
    this(new Long(-1L));
  }

  public TcManticVersionesDto(Long key) {
    this(null, null, null, new Long(-1L), null, null, null);
    setKey(key);
  }

  public TcManticVersionesDto(Long idActiva, Long idUsuario, String observaciones, Long idVersion, String nombre, String version, Timestamp vigencia) {
    setIdActiva(idActiva);
    setIdUsuario(idUsuario);
    setObservaciones(observaciones);
    setIdVersion(idVersion);
    setNombre(nombre);
    setVersion(version);
    setVigencia(vigencia);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdActiva(Long idActiva) {
    this.idActiva = idActiva;
  }

  public Long getIdActiva() {
    return idActiva;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setIdVersion(Long idVersion) {
    this.idVersion = idVersion;
  }

  public Long getIdVersion() {
    return idVersion;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getVersion() {
    return version;
  }

  public Timestamp getVigencia() {
    return vigencia;
  }

  public void setVigencia(Timestamp vigencia) {
    this.vigencia = vigencia;
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
  	return getIdVersion();
  }

  @Override
  public void setKey(Long key) {
  	this.idVersion = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdActiva());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVersion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getVersion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getVigencia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idActiva", getIdActiva());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idVersion", getIdVersion());
		regresar.put("nombre", getNombre());
		regresar.put("version", getVersion());
		regresar.put("vigencia", getVigencia());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
      getIdActiva(), getIdUsuario(), getObservaciones(), getIdVersion(), getNombre(), getVersion(), getVigencia(), getRegistro()
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
    regresar.append("idVersion~");
    regresar.append(getIdVersion());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdVersion());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticVersionesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdVersion()!= null && getIdVersion()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticVersionesDto other = (TcManticVersionesDto) obj;
    if (getIdVersion() != other.idVersion && (getIdVersion() == null || !getIdVersion().equals(other.idVersion))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdVersion() != null ? getIdVersion().hashCode() : 0);
    return hash;
  }

}


