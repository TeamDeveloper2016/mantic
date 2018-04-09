package mx.org.kaana.kajool.procesos.beans;

import java.io.Serializable;
import java.util.Calendar;
import mx.org.kaana.libs.formato.Fecha;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 14/10/2016
 *@time 10:18:40 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Usuario implements Serializable {

  private static final long serialVersionUID= -767792783193367978L;
  private Long idGrupo;
  private Long idPerfil;
  private String perfil;
  private String session;
  private String cuenta;
  private String nombre;
  private Long idEntidad;
  private Long claveEntidad;
  private String entidad;  	
  private Calendar hora;
  private Integer tipo;
  private Integer mensaje;
  private boolean invalidado;
  private String texto;
  private String grupo;

  public Usuario() {
    this(null, null, 1);
  } // Usuario

  public Usuario(String session, String cuenta, Integer tipo) {
    this(-1L, -1L, null, session, cuenta, null, -1L, -1L, null, null, tipo, -1, false, null, null);
  } // Usuario

  public Usuario(Long idGrupo, Long idPerfil, String perfil, String session, String cuenta, String nombre, Long idEntidad, Long claveEntidad, String entidad, Calendar hora, Integer tipo, Integer mensaje, boolean invalidado, String texto, String grupo) {
    this.idGrupo     = idGrupo;
    this.idPerfil    = idPerfil;
    this.perfil      = perfil;
    this.session     = session;
    this.cuenta      = cuenta;
    this.nombre      = nombre;
    this.idEntidad   = idEntidad;
    this.claveEntidad= claveEntidad;
    this.entidad     = entidad;
    this.hora        = hora;
    this.tipo        = tipo;
    this.mensaje     = mensaje;
    this.invalidado  = invalidado;
    this.texto       = texto;
    this.grupo       = grupo;
  }

  public Long getIdGrupo() {
    return idGrupo;
  }

  public void setIdGrupo(Long idGrupo) {
    this.idGrupo= idGrupo;
  }

  public Long getIdPerfil() {
    return idPerfil;
  }

  public void setIdPerfil(Long idPerfil) {
    this.idPerfil= idPerfil;
  }

  public String getPerfil() {
    return perfil;
  }

  public void setPerfil(String perfil) {
    this.perfil= perfil;
  }

  public String getSession() {
    return session;
  }

  public void setSession(String session) {
    this.session= session;
  }

  public String getCuenta() {
    return cuenta;
  }

  public void setCuenta(String cuenta) {
    this.cuenta= cuenta;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre= nombre;
  }

  public Long getIdEntidad() {
    return idEntidad;
  }

  public void setIdEntidad(Long idEntidad) {
    this.idEntidad= idEntidad;
  }

  public Long getClaveEntidad() {
    return claveEntidad;
  }

  public void setClaveEntidad(Long claveEntidad) {
    this.claveEntidad= claveEntidad;
  }

  public String getEntidad() {
    return entidad;
  }

  public void setEntidad(String entidad) {
    this.entidad= entidad;
  }

  public Calendar getHora() {
    return hora;
  }

  public void setHora(Calendar hora) {
    this.hora= hora;
  }

  public Integer getTipo() {
    return tipo;
  }

  public void setTipo(Integer tipo) {
    this.tipo= tipo;
  }

  public Integer getMensaje() {
    return mensaje;
  }

  public void setMensaje(Integer mensaje) {
    this.mensaje= mensaje;
  }

  public boolean isInvalidado() {
    return invalidado;
  }

  public void setInvalidado(boolean invalidado) {
    this.invalidado= invalidado;
  }

  public String getTexto() {
    return texto;
  }

  public void setTexto(String texto) {
    this.texto= texto;
  }

  public String getGrupo() {
    return grupo;
  }

  public void setGrupo(String grupo) {
    this.grupo= grupo;
  }

  public String getFecha() {
    return Fecha.formatear(Fecha.FECHA_NOMBRE_DIA, this.hora);
  }

  public String getTiempo() {
    return Fecha.formatear(Fecha.HORA_CORTA, this.hora);
  }

  public Long getMinutos() {
    return Fecha.diferenciaMinutos(this.getHora().getTimeInMillis(), Calendar.getInstance().getTimeInMillis());
  }

  public boolean isExclusivo() {
    return getTipo()!= 0;
  }

  @Override
  public String toString() {
    StringBuilder sb= new StringBuilder();
    sb.append(isExclusivo()? "*": "");
    sb.append(getSession());
    sb.append("[");
    sb.append(getCuenta());
    sb.append(",Tipo: ");
    sb.append(getTipo());
    sb.append(",Mensaje: ");
    sb.append(getMensaje());
    sb.append(",Hora: ");
    sb.append(Fecha.formatear(Fecha.FECHA_HORA, getHora()));
    sb.append("]");
    return sb.toString();
  } // toString

  public void clear() {
    this.cuenta= null;
    this.texto = null;
    this.tipo  = -1;
  } // clear

  @Override
  public void finalize() {
    this.cuenta = null;
    this.hora= null;
  } // finalize
}
