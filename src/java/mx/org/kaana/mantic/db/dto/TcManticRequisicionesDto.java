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
@Table(name="tc_mantic_requisiciones")
public class TcManticRequisicionesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="consecutivo")
  private String consecutivo;
  @Column (name="fecha_pedido")
  private Date fechaPedido;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="id_requisicion_estatus")
  private Long idRequisicionEstatus;
  @Column (name="fecha_entregada")
  private Date fechaEntregada;
  @Column (name="orden")
  private Long orden;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_requisicion")
  private Long idRequisicion;
  @Column (name="id_solicita")
  private Long idSolicita;
  @Column (name="ejercicio")
  private Long ejercicio;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticRequisicionesDto() {
    this(new Long(-1L));
  }

  public TcManticRequisicionesDto(Long key) {
    this(null, new Date(Calendar.getInstance().getTimeInMillis()), null, null, null, null, new Date(Calendar.getInstance().getTimeInMillis()), null, new Long(-1L), null, null);
    setKey(key);
  }

  public TcManticRequisicionesDto(String consecutivo, Date fechaPedido, Long idUsuario, String observaciones, Long idEmpresa, Long idRequisicionEstatus, Date fechaEntregada, Long orden, Long idRequisicion, Long idSolicita, Long ejercicio) {
    setConsecutivo(consecutivo);
    setFechaPedido(fechaPedido);
    setIdUsuario(idUsuario);
    setObservaciones(observaciones);
    setIdEmpresa(idEmpresa);
    setIdRequisicionEstatus(idRequisicionEstatus);
    setFechaEntregada(fechaEntregada);
    setOrden(orden);
    setIdRequisicion(idRequisicion);
    setIdSolicita(idSolicita);
    setEjercicio(ejercicio);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setConsecutivo(String consecutivo) {
    this.consecutivo = consecutivo;
  }

  public String getConsecutivo() {
    return consecutivo;
  }

  public void setFechaPedido(Date fechaPedido) {
    this.fechaPedido = fechaPedido;
  }

  public Date getFechaPedido() {
    return fechaPedido;
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

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }

  public Long getIdEmpresa() {
    return idEmpresa;
  }

  public void setIdRequisicionEstatus(Long idRequisicionEstatus) {
    this.idRequisicionEstatus = idRequisicionEstatus;
  }

  public Long getIdRequisicionEstatus() {
    return idRequisicionEstatus;
  }

  public void setFechaEntregada(Date fechaEntregada) {
    this.fechaEntregada = fechaEntregada;
  }

  public Date getFechaEntregada() {
    return fechaEntregada;
  }

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
  }

  public void setIdRequisicion(Long idRequisicion) {
    this.idRequisicion = idRequisicion;
  }

  public Long getIdRequisicion() {
    return idRequisicion;
  }

  public void setIdSolicita(Long idSolicita) {
    this.idSolicita = idSolicita;
  }

  public Long getIdSolicita() {
    return idSolicita;
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

  @Transient
  @Override
  public Long getKey() {
  	return getIdRequisicion();
  }

  @Override
  public void setKey(Long key) {
  	this.idRequisicion = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFechaPedido());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdRequisicionEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFechaEntregada());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdRequisicion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdSolicita());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEjercicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("fechaPedido", getFechaPedido());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("idRequisicionEstatus", getIdRequisicionEstatus());
		regresar.put("fechaEntregada", getFechaEntregada());
		regresar.put("orden", getOrden());
		regresar.put("idRequisicion", getIdRequisicion());
		regresar.put("idSolicita", getIdSolicita());
		regresar.put("ejercicio", getEjercicio());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getConsecutivo(), getFechaPedido(), getIdUsuario(), getObservaciones(), getIdEmpresa(), getIdRequisicionEstatus(), getFechaEntregada(), getOrden(), getIdRequisicion(), getIdSolicita(), getEjercicio(), getRegistro()
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
    regresar.append("idRequisicion~");
    regresar.append(getIdRequisicion());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdRequisicion());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticRequisicionesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdRequisicion()!= null && getIdRequisicion()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticRequisicionesDto other = (TcManticRequisicionesDto) obj;
    if (getIdRequisicion() != other.idRequisicion && (getIdRequisicion() == null || !getIdRequisicion().equals(other.idRequisicion))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdRequisicion() != null ? getIdRequisicion().hashCode() : 0);
    return hash;
  }

}


