package mx.org.kaana.kalan.catalogos.pacientes.citas.beans;

import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Objects;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 22/03/2023
 *@time 06:40:50 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Paciente extends TcManticClientesDto implements Serializable {

  private static final long serialVersionUID = 933154433403709712L;

  private Long idCita;
  private Timestamp inicio;
  private Timestamp termino;
  private String celular;
  private String correo;
  private Long recordatorio;
  private Long notificacion;
  private String comentarios;
  
  private UISelectEntity ikAtendio;
  
  public Paciente() {
    this(-1L);
  }

  public Paciente(Date fecha) {
    this(-1L, new Timestamp(fecha.getTime()), new Timestamp(fecha.getTime()), "", "", -1L);
  }

  public Paciente(Long key) {
    this(key, new Timestamp(Calendar.getInstance().getTimeInMillis()), new Timestamp(Calendar.getInstance().getTimeInMillis()), "", "", -1L);
  }

  public Paciente(Long key, Timestamp inicio, Timestamp termino, String celular, String correo, Long idCita) {
    super(key);
    this.inicio  = inicio;
    this.termino = termino;
    this.celular = celular;
    this.correo  = correo;
    this.idCita  = idCita;
    this.recordatorio = 24L;
    this.notificacion = 2L;
    this.comentarios  = "";
    this.init();
  }

  public Long getIdCita() {
    return idCita;
  }

  public void setIdCita(Long idCita) {
    this.idCita = idCita;
  }
  
  public Timestamp getInicio() {
    return inicio;
  }

  public void setInicio(Timestamp inicio) {
    this.inicio = inicio;
  }

  public Timestamp getTermino() {
    return termino;
  }

  public void setTermino(Timestamp termino) {
    this.termino = termino;
  }

  public String getCelular() {
    return celular;
  }

  public void setCelular(String celular) {
    this.celular = celular;
  }

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public UISelectEntity getIkAtendio() {
    return ikAtendio;
  }

  public void setIkAtendio(UISelectEntity ikAtendio) {
    this.ikAtendio = ikAtendio;
  }

  public Long getRecordatorio() {
    return recordatorio;
  }

  public void setRecordatorio(Long recordatorio) {
    this.recordatorio = recordatorio;
  }

  public Long getNotificacion() {
    return notificacion;
  }

  public void setNotificacion(Long notificacion) {
    this.notificacion = notificacion;
  }

  public String getComentarios() {
    return comentarios;
  }

  public void setComentarios(String comentarios) {
    this.comentarios = comentarios;
  }

  private void init() {
    Calendar minutos= Calendar.getInstance();
    if(minutos.get(Calendar.MINUTE)<= 30)
      minutos.set(Calendar.MINUTE, 30);
    else
      if(minutos.get(Calendar.MINUTE)> 30)
        minutos.set(Calendar.MINUTE, 60);
    minutos.set(Calendar.SECOND, 0);
    this.inicio= new Timestamp(minutos.getTimeInMillis());
    minutos.add(Calendar.MINUTE, 30);
    this.termino= new Timestamp(minutos.getTimeInMillis());
  }

  public Paciente clon() {
    Paciente regresar= new Paciente(this.getKey(), getInicio(), getTermino(), getCelular(), getCorreo(), getIdCita());
    regresar.setIkAtendio(new UISelectEntity(this.getIkAtendio().getKey()));
    return regresar;
  }  
  
  public Boolean equals(Paciente paciente) {
    return Objects.equals(this.getKey(), paciente.getKey()) &&
           Objects.equals(this.getInicio(), paciente.getInicio()) &&
           Objects.equals(this.getTermino(), paciente.getTermino()) &&
           Objects.equals(this.getCelular(), paciente.getCelular()) &&
           Objects.equals(this.getCorreo(), paciente.getCorreo()) &&
           Objects.equals(this.getIkAtendio(), paciente.getIkAtendio());
  }
  
}
