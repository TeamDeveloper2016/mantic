package mx.org.kaana.mantic.db.dto;

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

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/10/2016
 *@time 11:58:22 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Entity
@Table(name="tc_mantic_domicilios")
public class TcManticDomiciliosDto implements IBaseDto, Serializable,Cloneable {
		
  private static final long serialVersionUID=1L;
  @Column (name="asentamiento")
  private String asentamiento;
  @Column (name="id_localidad")
  private Long idLocalidad;
  @Column (name="codigo_postal")
  private String codigoPostal;
  @Column (name="latitud")
  private String latitud;
  @Column (name="entre_calle")
  private String entreCalle;
  @Column (name="calle")
  private String calle;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_domicilio")
  private Long idDomicilio;
  @Column (name="numero_interior")
  private String numeroInterior;  
  @Column (name="y_calle")
  private String ycalle;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="longitud")
  private String longitud;
  @Column (name="numero_exterior")
  private String numeroExterior;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="observaciones")
  private String observaciones;  

  public TcManticDomiciliosDto() {
    this(new Long(-1L));
  }

  public TcManticDomiciliosDto(Long key) {
    this(null, null, null, null, null, null, new Long(-1L), null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticDomiciliosDto(String asentamiento, Long idLocalidad, String codigoPostal, String latitud, String entreCalle, String calle, Long idDomicilio, String numeroInterior,  String ycalle, String longitud, String numeroExterior, Long idUsuario, String observaciones) {
    setAsentamiento(asentamiento);
    setIdLocalidad(idLocalidad);
    setCodigoPostal(codigoPostal);
    setLatitud(latitud);
    setEntreCalle(entreCalle);
    setCalle(calle);
    setIdDomicilio(idDomicilio);
    setNumeroInterior(numeroInterior);
    setYcalle(ycalle);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setLongitud(longitud);
    setNumeroExterior(numeroExterior);
    setIdUsuario(idUsuario);
    setObservaciones(observaciones);
  }
	
  public void setAsentamiento(String asentamiento) {
    this.asentamiento = asentamiento;
  }

  public String getAsentamiento() {
    return asentamiento;
  }

  public void setIdLocalidad(Long idLocalidad) {
    this.idLocalidad = idLocalidad;
  }

  public Long getIdLocalidad() {
    return idLocalidad;
  }

  public void setCodigoPostal(String codigoPostal) {
    this.codigoPostal = codigoPostal;
  }

  public String getCodigoPostal() {
    return codigoPostal;
  }

  public void setLatitud(String latitud) {
    this.latitud = latitud;
  }

  public String getLatitud() {
    return latitud;
  }

  public void setEntreCalle(String entreCalle) {
    this.entreCalle = entreCalle;
  }

  public String getEntreCalle() {
    return entreCalle;
  }

  public void setCalle(String calle) {
    this.calle = calle;
  }

  public String getCalle() {
    return calle;
  }

  public void setIdDomicilio(Long idDomicilio) {
    this.idDomicilio = idDomicilio;
  }

  public Long getIdDomicilio() {
    return idDomicilio;
  }

  public void setNumeroInterior(String numeroInterior) {
    this.numeroInterior = numeroInterior;
  }

  public String getNumeroInterior() {
    return numeroInterior;
  }  

  public void setYcalle(String ycalle) {
    this.ycalle = ycalle;
  }

  public String getYcalle() {
    return ycalle;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setLongitud(String longitud) {
    this.longitud = longitud;
  }

  public String getLongitud() {
    return longitud;
  }

  public void setNumeroExterior(String numeroExterior) {
    this.numeroExterior = numeroExterior;
  }

  public String getNumeroExterior() {
    return numeroExterior;
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

  @Transient
  @Override
  public Long getKey() {
  	return getIdDomicilio();
  }

  @Override
  public void setKey(Long key) {
  	this.idDomicilio = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getAsentamiento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdLocalidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCodigoPostal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getLatitud());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEntreCalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdDomicilio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNumeroInterior());
		regresar.append(Constantes.SEPARADOR);		
		regresar.append(getYcalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getLongitud());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNumeroExterior());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());		
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("asentamiento", getAsentamiento());
		regresar.put("idLocalidad", getIdLocalidad());
		regresar.put("codigoPostal", getCodigoPostal());
		regresar.put("latitud", getLatitud());
		regresar.put("entreCalle", getEntreCalle());
		regresar.put("calle", getCalle());
		regresar.put("idDomicilio", getIdDomicilio());
		regresar.put("numeroInterior", getNumeroInterior());
		regresar.put("ycalle", getYcalle());
		regresar.put("registro", getRegistro());
		regresar.put("longitud", getLongitud());
		regresar.put("numeroExterior", getNumeroExterior());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("observaciones", getObservaciones());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getAsentamiento(), getIdLocalidad(), getCodigoPostal(), getLatitud(), getEntreCalle(), getCalle(), getIdDomicilio(), getNumeroInterior(), getYcalle(), getRegistro(), getLongitud(), getNumeroExterior(), getIdUsuario(), getObservaciones()
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
    regresar.append("idDomicilio~");
    regresar.append(getIdDomicilio());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdDomicilio());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticDomiciliosDto.class;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone(); //To change body of generated methods, choose Tools | Templates.
  }

  
  
  @Override
  public boolean isValid() {
  	return getIdDomicilio()!= null && getIdDomicilio()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticDomiciliosDto other = (TcManticDomiciliosDto) obj;
    if (getIdDomicilio() != other.idDomicilio && (getIdDomicilio() == null || !getIdDomicilio().equals(other.idDomicilio))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdDomicilio() != null ? getIdDomicilio().hashCode() : 0);
    return hash;
  }
}


