package mx.org.kaana.kajool.db.dto;

import java.io.Serializable;
import java.sql.Date;
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
@Table(name="tc_janal_mensajes")
public class TcJanalMensajesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="DIAS")
  private String dias;
  @Column (name="ID_TIPO_MENSAJE")
  private Long idTipoMensaje;
  @Column (name="NOMBRE")
  private String nombre;
  @Column (name="VIGENCIA_FIN")
  private Date vigenciaFin;
  @Column (name="FECHA_REPITE")
  private Date fechaRepite;
  @Column (name="ID_USUARIO")
  private Long idUsuario;
  @Column (name="DESCRIPCION")
  private String descripcion;
  @Column (name="PERIODO_REPITE")
  private Long periodoRepite;
  @Column (name="PERIODO")
  private Long periodo;
  @Column (name="URL_SEGUIMIENTO")
  private String urlSeguimiento;
  @Column (name="ID_USUARIO_MODIFICA")
  private Long idUsuarioModifica;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idMensaje_sequence")
  //@SequenceGenerator(name="idMensaje_sequence",sequenceName="SEQ_TC_JANAL_MENSAJES" , allocationSize=1 )
  @Column (name="ID_MENSAJE")
  private Long idMensaje;
  @Column (name="VIGENCIA_INI")
  private Date vigenciaIni;
  @Column (name="REGISTRO")
  private Timestamp registro;
  @Column (name="ACTUALIZACION")
  private String actualizacion;
  @Column (name="ID_PRIORIDAD")
  private Long idPrioridad;

  public TcJanalMensajesDto() {
    this(new Long(-1L));
  }

  public TcJanalMensajesDto(Long key) {
    this(null, null, null, new Date(Calendar.getInstance().getTimeInMillis()), new Date(Calendar.getInstance().getTimeInMillis()), null, null, null, null, null, null, new Long(-1L), new Date(Calendar.getInstance().getTimeInMillis()), null, null);
    setKey(key);
  }

  public TcJanalMensajesDto(String dias, Long idTipoMensaje, String nombre, Date vigenciaFin, Date fechaRepite, Long idUsuario, String descripcion, Long periodoRepite, Long periodo, String urlSeguimiento, Long idUsuarioModifica, Long idMensaje, Date vigenciaIni, String actualizacion, Long idPrioridad) {
    setDias(dias);
    setIdTipoMensaje(idTipoMensaje);
    setNombre(nombre);
    setVigenciaFin(vigenciaFin);
    setFechaRepite(fechaRepite);
    setIdUsuario(idUsuario);
    setDescripcion(descripcion);
    setPeriodoRepite(periodoRepite);
    setPeriodo(periodo);
    setUrlSeguimiento(urlSeguimiento);
    setIdUsuarioModifica(idUsuarioModifica);
    setIdMensaje(idMensaje);
    setVigenciaIni(vigenciaIni);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setActualizacion(actualizacion);
    setIdPrioridad(idPrioridad);
  }
	
  public void setDias(String dias) {
    this.dias = dias;
  }

  public String getDias() {
    return dias;
  }

  public void setIdTipoMensaje(Long idTipoMensaje) {
    this.idTipoMensaje = idTipoMensaje;
  }

  public Long getIdTipoMensaje() {
    return idTipoMensaje;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public void setVigenciaFin(Date vigenciaFin) {
    this.vigenciaFin = vigenciaFin;
  }

  public Date getVigenciaFin() {
    return vigenciaFin;
  }

  public void setFechaRepite(Date fechaRepite) {
    this.fechaRepite = fechaRepite;
  }

  public Date getFechaRepite() {
    return fechaRepite;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setPeriodoRepite(Long periodoRepite) {
    this.periodoRepite = periodoRepite;
  }

  public Long getPeriodoRepite() {
    return periodoRepite;
  }

  public void setPeriodo(Long periodo) {
    this.periodo = periodo;
  }

  public Long getPeriodo() {
    return periodo;
  }

  public void setUrlSeguimiento(String urlSeguimiento) {
    this.urlSeguimiento = urlSeguimiento;
  }

  public String getUrlSeguimiento() {
    return urlSeguimiento;
  }

  public void setIdUsuarioModifica(Long idUsuarioModifica) {
    this.idUsuarioModifica = idUsuarioModifica;
  }

  public Long getIdUsuarioModifica() {
    return idUsuarioModifica;
  }

  public void setIdMensaje(Long idMensaje) {
    this.idMensaje = idMensaje;
  }

  public Long getIdMensaje() {
    return idMensaje;
  }

  public void setVigenciaIni(Date vigenciaIni) {
    this.vigenciaIni = vigenciaIni;
  }

  public Date getVigenciaIni() {
    return vigenciaIni;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setActualizacion(String actualizacion) {
    this.actualizacion = actualizacion;
  }

  public String getActualizacion() {
    return actualizacion;
  }

  public void setIdPrioridad(Long idPrioridad) {
    this.idPrioridad = idPrioridad;
  }

  public Long getIdPrioridad() {
    return idPrioridad;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdMensaje();
  }

  @Override
  public void setKey(Long key) {
  	this.idMensaje = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDias());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoMensaje());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getVigenciaFin());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFechaRepite());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPeriodoRepite());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPeriodo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getUrlSeguimiento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuarioModifica());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMensaje());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getVigenciaIni());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getActualizacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPrioridad());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("dias", getDias());
		regresar.put("idTipoMensaje", getIdTipoMensaje());
		regresar.put("nombre", getNombre());
		regresar.put("vigenciaFin", getVigenciaFin());
		regresar.put("fechaRepite", getFechaRepite());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("descripcion", getDescripcion());
		regresar.put("periodoRepite", getPeriodoRepite());
		regresar.put("periodo", getPeriodo());
		regresar.put("urlSeguimiento", getUrlSeguimiento());
		regresar.put("idUsuarioModifica", getIdUsuarioModifica());
		regresar.put("idMensaje", getIdMensaje());
		regresar.put("vigenciaIni", getVigenciaIni());
		regresar.put("registro", getRegistro());
		regresar.put("actualizacion", getActualizacion());
		regresar.put("idPrioridad", getIdPrioridad());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getDias(), getIdTipoMensaje(), getNombre(), getVigenciaFin(), getFechaRepite(), getIdUsuario(), getDescripcion(), getPeriodoRepite(), getPeriodo(), getUrlSeguimiento(), getIdUsuarioModifica(), getIdMensaje(), getVigenciaIni(), getRegistro(), getActualizacion(), getIdPrioridad()
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
    regresar.append("idMensaje~");
    regresar.append(getIdMensaje());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdMensaje());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcJanalMensajesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdMensaje()!= null && getIdMensaje()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcJanalMensajesDto other = (TcJanalMensajesDto) obj;
    if (getIdMensaje() != other.idMensaje && (getIdMensaje() == null || !getIdMensaje().equals(other.idMensaje))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdMensaje() != null ? getIdMensaje().hashCode() : 0);
    return hash;
  }

}


