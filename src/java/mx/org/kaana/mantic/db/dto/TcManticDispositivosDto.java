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
@Table(name="tc_mantic_dispositivos")
public class TcManticDispositivosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_activo")
  private Long idActivo;
  @Column (name="original")
  private String original;
  @Column (name="dispositivo")
  private String dispositivo;
  @Column (name="semilla")
  private String semilla;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_dispositivo")
  private Long idDispositivo;
  @Column (name="cuenta")
  private String cuenta;
  @Column (name="imei")
  private String imei;
  @Column (name="id_version")
  private Long idVersion;
  @Column (name="nombre")
  private String nombre;
  @Column (name="version")
  private String version;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticDispositivosDto() {
    this(new Long(-1L));
  }

  public TcManticDispositivosDto(Long key) {
    this(null, null, null, null, new Long(-1L), null, null, null, null, null);
    setKey(key);
  }

  public TcManticDispositivosDto(Long idActivo, String original, String dispositivo, String semilla, Long idDispositivo, String cuenta, String imei, Long idVersion, String nombre, String version) {
    setIdActivo(idActivo);
    setOriginal(original);
    setDispositivo(dispositivo);
    setSemilla(semilla);
    setIdDispositivo(idDispositivo);
    setCuenta(cuenta);
    setImei(imei);
    setIdVersion(idVersion);
    setNombre(nombre);
    setVersion(version);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdActivo(Long idActivo) {
    this.idActivo = idActivo;
  }

  public Long getIdActivo() {
    return idActivo;
  }

  public void setOriginal(String original) {
    this.original = original;
  }

  public String getOriginal() {
    return original;
  }

  public void setDispositivo(String dispositivo) {
    this.dispositivo = dispositivo;
  }

  public String getDispositivo() {
    return dispositivo;
  }

  public void setSemilla(String semilla) {
    this.semilla = semilla;
  }

  public String getSemilla() {
    return semilla;
  }

  public void setIdDispositivo(Long idDispositivo) {
    this.idDispositivo = idDispositivo;
  }

  public Long getIdDispositivo() {
    return idDispositivo;
  }

  public void setCuenta(String cuenta) {
    this.cuenta = cuenta;
  }

  public String getCuenta() {
    return cuenta;
  }

  public void setImei(String imei) {
    this.imei = imei;
  }

  public String getImei() {
    return imei;
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

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdDispositivo();
  }

  @Override
  public void setKey(Long key) {
  	this.idDispositivo = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdActivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOriginal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDispositivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSemilla());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdDispositivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCuenta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImei());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVersion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getVersion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idActivo", getIdActivo());
		regresar.put("original", getOriginal());
		regresar.put("dispositivo", getDispositivo());
		regresar.put("semilla", getSemilla());
		regresar.put("idDispositivo", getIdDispositivo());
		regresar.put("cuenta", getCuenta());
		regresar.put("imei", getImei());
		regresar.put("idVersion", getIdVersion());
		regresar.put("nombre", getNombre());
		regresar.put("version", getVersion());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[] {
      getIdActivo(), getOriginal(), getDispositivo(), getSemilla(), getIdDispositivo(), getCuenta(), getImei(), getIdVersion(), getNombre(), getVersion(), getRegistro()
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
    regresar.append("idDispositivo~");
    regresar.append(getIdDispositivo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdDispositivo());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticDispositivosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdDispositivo()!= null && getIdDispositivo()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticDispositivosDto other = (TcManticDispositivosDto) obj;
    if (getIdDispositivo() != other.idDispositivo && (getIdDispositivo() == null || !getIdDispositivo().equals(other.idDispositivo))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdDispositivo() != null ? getIdDispositivo().hashCode() : 0);
    return hash;
  }

}


