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
@Table(name="tc_mantic_transferencias")
public class TcManticTransferenciasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_solicito")
  private Long idSolicito;
  @Column (name="id_transferencia_estatus")
  private Long idTransferenciaEstatus;
  @Column (name="id_transferencia_tipo")
  private Long idTransferenciaTipo;
  @Column (name="ejercicio")
  private Long ejercicio;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="consecutivo")
  private String consecutivo;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_almacen")
  private Long idAlmacen;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="id_destino")
  private Long idDestino;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="orden")
  private Long orden;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_transferencia")
  private Long idTransferencia;

  public TcManticTransferenciasDto() {
    this(new Long(-1L));
  }

  public TcManticTransferenciasDto(Long key) {
    this(null, 1L, null, null, null, null, null, null, null, null, null, new Long(-1L));
    setKey(key);
  }

  public TcManticTransferenciasDto(Long idSolicito, Long idTransferenciaEstatus, Long idTransferenciaTipo, Long ejercicio, String consecutivo, Long idUsuario, Long idAlmacen, String observaciones, Long idDestino, Long idEmpresa, Long orden, Long idTransferencia) {
    setIdSolicito(idSolicito);
    setIdTransferenciaEstatus(idTransferenciaEstatus);
    setIdTransferenciaTipo(idTransferenciaTipo);
    setEjercicio(ejercicio);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setConsecutivo(consecutivo);
    setIdUsuario(idUsuario);
    setIdAlmacen(idAlmacen);
    setObservaciones(observaciones);
    setIdDestino(idDestino);
    setIdEmpresa(idEmpresa);
    setOrden(orden);
    setIdTransferencia(idTransferencia);
  }
	
  public void setIdSolicito(Long idSolicito) {
    this.idSolicito = idSolicito;
  }

  public Long getIdSolicito() {
    return idSolicito;
  }

  public void setIdTransferenciaEstatus(Long idTransferenciaEstatus) {
    this.idTransferenciaEstatus = idTransferenciaEstatus;
  }

  public Long getIdTransferenciaEstatus() {
    return idTransferenciaEstatus;
  }

  public void setIdTransferenciaTipo(Long idTransferenciaTipo) {
    this.idTransferenciaTipo = idTransferenciaTipo;
  }

  public Long getIdTransferenciaTipo() {
    return idTransferenciaTipo;
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

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdAlmacen(Long idAlmacen) {
    this.idAlmacen = idAlmacen;
  }

  public Long getIdAlmacen() {
    return idAlmacen;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setIdDestino(Long idDestino) {
    this.idDestino = idDestino;
  }

  public Long getIdDestino() {
    return idDestino;
  }

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }

  public Long getIdEmpresa() {
    return idEmpresa;
  }

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
  }

  public void setIdTransferencia(Long idTransferencia) {
    this.idTransferencia = idTransferencia;
  }

  public Long getIdTransferencia() {
    return idTransferencia;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdTransferencia();
  }

  @Override
  public void setKey(Long key) {
  	this.idTransferencia = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdSolicito());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTransferenciaEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTransferenciaTipo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEjercicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAlmacen());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdDestino());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTransferencia());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idSolicito", getIdSolicito());
		regresar.put("idTransferenciaEstatus", getIdTransferenciaEstatus());
		regresar.put("idTransferenciaTipo", getIdTransferenciaTipo());
		regresar.put("ejercicio", getEjercicio());
		regresar.put("registro", getRegistro());
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idAlmacen", getIdAlmacen());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idDestino", getIdDestino());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("orden", getOrden());
		regresar.put("idTransferencia", getIdTransferencia());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdSolicito(), getIdTransferenciaEstatus(), getIdTransferenciaTipo(), getEjercicio(), getRegistro(), getConsecutivo(), getIdUsuario(), getIdAlmacen(), getObservaciones(), getIdDestino(), getIdEmpresa(), getOrden(), getIdTransferencia()
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
    regresar.append("idTransferencia~");
    regresar.append(getIdTransferencia());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdTransferencia());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticTransferenciasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdTransferencia()!= null && getIdTransferencia()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticTransferenciasDto other = (TcManticTransferenciasDto) obj;
    if (getIdTransferencia() != other.idTransferencia && (getIdTransferencia() == null || !getIdTransferencia().equals(other.idTransferencia))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdTransferencia() != null ? getIdTransferencia().hashCode() : 0);
    return hash;
  }

}


