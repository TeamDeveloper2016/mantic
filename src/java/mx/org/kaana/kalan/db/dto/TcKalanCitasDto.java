package mx.org.kaana.kalan.db.dto;

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
@Table(name="tc_kalan_citas")
public class TcKalanCitasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_cliente")
  private Long idCliente;
  @Column (name="id_cita_estatus")
  private Long idCitaEstatus;
  @Column (name="inicio")
  private Timestamp inicio;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_cita")
  private Long idCita;
  @Column (name="notificacion")
  private Long notificacion;
  @Column (name="ejercicio")
  private Long ejercicio;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="consecutivo")
  private String consecutivo;
  @Column (name="id_atendio")
  private Long idAtendio;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="recordatorio")
  private Long recordatorio;
  @Column (name="otro")
  private String otro;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="termino")
  private Timestamp termino;
  @Column (name="orden")
  private Long orden;
  @Column (name="id_venta")
  private Long idVenta;

  public TcKalanCitasDto() {
    this(new Long(-1L));
  }

  public TcKalanCitasDto(Long key) {
    this(null, null, new Timestamp(Calendar.getInstance().getTimeInMillis()), new Long(-1L), null, null, null, null, null, null, null, null, new Timestamp(Calendar.getInstance().getTimeInMillis()), null, null);
    setKey(key);
  }

  public TcKalanCitasDto(Long idCliente, Long idCitaEstatus, Timestamp inicio, Long idCita, Long notificacion, Long ejercicio, String consecutivo, Long idAtendio, Long idUsuario, Long recordatorio, String otro, String observaciones, Timestamp termino, Long orden, Long idVenta) {
    setIdCliente(idCliente);
    setIdCitaEstatus(idCitaEstatus);
    setInicio(inicio);
    setIdCita(idCita);
    setNotificacion(notificacion);
    setEjercicio(ejercicio);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setConsecutivo(consecutivo);
    setIdAtendio(idAtendio);
    setIdUsuario(idUsuario);
    setRecordatorio(recordatorio);
    setOtro(otro);
    setObservaciones(observaciones);
    setTermino(termino);
    setOrden(orden);
    setIdVenta(idVenta);
  }
	
  public void setIdCliente(Long idCliente) {
    this.idCliente = idCliente;
  }

  public Long getIdCliente() {
    return idCliente;
  }

  public void setIdCitaEstatus(Long idCitaEstatus) {
    this.idCitaEstatus = idCitaEstatus;
  }

  public Long getIdCitaEstatus() {
    return idCitaEstatus;
  }

  public void setInicio(Timestamp inicio) {
    this.inicio = inicio;
  }

  public Timestamp getInicio() {
    return inicio;
  }

  public void setIdCita(Long idCita) {
    this.idCita = idCita;
  }

  public Long getIdCita() {
    return idCita;
  }

  public void setNotificacion(Long notificacion) {
    this.notificacion = notificacion;
  }

  public Long getNotificacion() {
    return notificacion;
  }

  public void setEjercicio(Long ejercicio) {
    this.ejercicio = ejercicio;
  }

  public Long getEjercicio() {
    return ejercicio;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setConsecutivo(String consecutivo) {
    this.consecutivo = consecutivo;
  }

  public String getConsecutivo() {
    return consecutivo;
  }

  public void setIdAtendio(Long idAtendio) {
    this.idAtendio = idAtendio;
  }

  public Long getIdAtendio() {
    return idAtendio;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setRecordatorio(Long recordatorio) {
    this.recordatorio = recordatorio;
  }

  public Long getRecordatorio() {
    return recordatorio;
  }

  public void setOtro(String otro) {
    this.otro = otro;
  }

  public String getOtro() {
    return otro;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setTermino(Timestamp termino) {
    this.termino = termino;
  }

  public Timestamp getTermino() {
    return termino;
  }

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
  }

  public void setIdVenta(Long idVenta) {
    this.idVenta = idVenta;
  }

  public Long getIdVenta() {
    return idVenta;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdCita();
  }

  @Override
  public void setKey(Long key) {
  	this.idCita = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdCliente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCitaEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getInicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCita());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNotificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEjercicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAtendio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRecordatorio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOtro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTermino());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVenta());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idCliente", getIdCliente());
		regresar.put("idCitaEstatus", getIdCitaEstatus());
		regresar.put("inicio", getInicio());
		regresar.put("idCita", getIdCita());
		regresar.put("notificacion", getNotificacion());
		regresar.put("ejercicio", getEjercicio());
		regresar.put("registro", getRegistro());
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("idAtendio", getIdAtendio());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("recordatorio", getRecordatorio());
		regresar.put("otro", getOtro());
		regresar.put("observaciones", getObservaciones());
		regresar.put("termino", getTermino());
		regresar.put("orden", getOrden());
		regresar.put("idVenta", getIdVenta());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdCliente(), getIdCitaEstatus(), getInicio(), getIdCita(), getNotificacion(), getEjercicio(), getRegistro(), getConsecutivo(), getIdAtendio(), getIdUsuario(), getRecordatorio(), getOtro(), getObservaciones(), getTermino(), getOrden(), getIdVenta()
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
    regresar.append("idCita~");
    regresar.append(getIdCita());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdCita());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcKalanCitasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdCita()!= null && getIdCita()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcKalanCitasDto other = (TcKalanCitasDto) obj;
    if (getIdCita() != other.idCita && (getIdCita() == null || !getIdCita().equals(other.idCita))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdCita() != null ? getIdCita().hashCode() : 0);
    return hash;
  }

}


