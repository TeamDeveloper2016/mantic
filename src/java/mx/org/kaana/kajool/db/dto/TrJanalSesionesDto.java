package mx.org.kaana.kajool.db.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

@Entity
@Table(name="tr_janal_sesiones")
public class TrJanalSesionesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="REGISTRO_FIN")
  private Timestamp registroFin;
  @Column (name="ID_USUARIO")
  private Long idUsuario;
  @Column (name="REGISTRO_INICIO")
  private Timestamp registroInicio;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idSesion_sequence")
  //@SequenceGenerator(name="idSesion_sequence",sequenceName="SEQ_TR_JANAL_SESIONES" , allocationSize=1 )
  @Column (name="ID_SESION")
  private Long idSesion;
  @Column (name="CUENTA")
  private String cuenta;
  @Column (name="PATH")
  private String path;
  @Column (name="INICIO")
  private Timestamp inicio;
  @Column (name="REGISTRO")
  private Timestamp registro;
  @Column (name="SESION")
  private String sesion;

  public TrJanalSesionesDto() {
    this(new Long(-1L));
  }

  public TrJanalSesionesDto(Long key) {
    this(new Timestamp(Calendar.getInstance().getTimeInMillis()), null, new Timestamp(Calendar.getInstance().getTimeInMillis()), new Long(-1L), null, null, new Timestamp(Calendar.getInstance().getTimeInMillis()), null);
    setKey(key);
  }

  public TrJanalSesionesDto(Timestamp registroFin, Long idUsuario, Timestamp registroInicio, Long idSesion, String cuenta, String path, Timestamp inicio, String sesion) {
    setRegistroFin(registroFin);
    setIdUsuario(idUsuario);
    setRegistroInicio(registroInicio);
    setIdSesion(idSesion);
    setCuenta(cuenta);
    setPath(path);
    setInicio(inicio);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setSesion(sesion);
  }

  public void setRegistroFin(Timestamp registroFin) {
    this.registroFin = registroFin;
  }

  public Timestamp getRegistroFin() {
    return registroFin;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setRegistroInicio(Timestamp registroInicio) {
    this.registroInicio = registroInicio;
  }

  public Timestamp getRegistroInicio() {
    return registroInicio;
  }

  public void setIdSesion(Long idSesion) {
    this.idSesion = idSesion;
  }

  public Long getIdSesion() {
    return idSesion;
  }

  public void setCuenta(String cuenta) {
    this.cuenta = cuenta;
  }

  public String getCuenta() {
    return cuenta;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }

  public void setInicio(Timestamp inicio) {
    this.inicio = inicio;
  }

  public Timestamp getInicio() {
    return inicio;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setSesion(String sesion) {
    this.sesion = sesion;
  }

  public String getSesion() {
    return sesion;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdSesion();
  }

  @Override
  public void setKey(Long key) {
  	this.idSesion = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getRegistroFin());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistroInicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdSesion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCuenta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPath());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getInicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSesion());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("registroFin", getRegistroFin());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("registroInicio", getRegistroInicio());
		regresar.put("idSesion", getIdSesion());
		regresar.put("cuenta", getCuenta());
		regresar.put("path", getPath());
		regresar.put("inicio", getInicio());
		regresar.put("registro", getRegistro());
		regresar.put("sesion", getSesion());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getRegistroFin(), getIdUsuario(), getRegistroInicio(), getIdSesion(), getCuenta(), getPath(), getInicio(), getRegistro(), getSesion()
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
    regresar.append("idSesion~");
    regresar.append(getIdSesion());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdSesion());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrJanalSesionesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdSesion()!= null && getIdSesion()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrJanalSesionesDto other = (TrJanalSesionesDto) obj;
    if (getIdSesion() != other.idSesion && (getIdSesion() == null || !getIdSesion().equals(other.idSesion))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdSesion() != null ? getIdSesion().hashCode() : 0);
    return hash;
  }

}


