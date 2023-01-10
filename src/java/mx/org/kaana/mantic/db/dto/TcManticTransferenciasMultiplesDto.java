package mx.org.kaana.mantic.db.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
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
@Table(name="tc_mantic_transferencias_multiples")
public class TcManticTransferenciasMultiplesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="consecutivo")
  private String consecutivo;
  @Column (name="id_solicito")
  private Long idSolicito;
  @Column (name="id_transferencia_multiple_estatus")
  private Long idTransferenciaMultipleEstatus;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_almacen")
  private Long idAlmacen;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="orden")
  private Long orden;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  @Column (name="id_transferencia_multiple")
  private Long idTransferenciaMultiple;
  @Column (name="ejercicio")
  private Long ejercicio;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticTransferenciasMultiplesDto() {
    this(new Long(-1L));
  }

  public TcManticTransferenciasMultiplesDto(Long key) {
    this(null, null, null, null, null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticTransferenciasMultiplesDto(String consecutivo, Long idSolicito, Long idTransferenciaMultipleEstatus, Long idUsuario, Long idAlmacen, String observaciones, Long idEmpresa, Long orden, Long idTransfereciaMultiple, Long ejercicio) {
    setConsecutivo(consecutivo);
    setIdSolicito(idSolicito);
    setIdTransferenciaMultipleEstatus(idTransferenciaMultipleEstatus);
    setIdUsuario(idUsuario);
    setIdAlmacen(idAlmacen);
    setObservaciones(observaciones);
    setIdEmpresa(idEmpresa);
    setOrden(orden);
    setIdTransferenciaMultiple(idTransferenciaMultiple);
    setEjercicio(ejercicio);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setConsecutivo(String consecutivo) {
    this.consecutivo = consecutivo;
  }

  public String getConsecutivo() {
    return consecutivo;
  }

  public void setIdSolicito(Long idSolicito) {
    this.idSolicito = idSolicito;
  }

  public Long getIdSolicito() {
    return idSolicito;
  }

  public void setIdTransferenciaMultipleEstatus(Long idTransferenciaMultipleEstatus) {
    this.idTransferenciaMultipleEstatus = idTransferenciaMultipleEstatus;
  }

  public Long getIdTransferenciaMultipleEstatus() {
    return idTransferenciaMultipleEstatus;
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

  public void setIdTransferenciaMultiple(Long idTransferenciaMultiple) {
    this.idTransferenciaMultiple = idTransferenciaMultiple;
  }

  public Long getIdTransferenciaMultiple() {
    return idTransferenciaMultiple;
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
  	return getIdTransferenciaMultiple();
  }

  @Override
  public void setKey(Long key) {
  	this.idTransferenciaMultiple = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdSolicito());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTransferenciaMultipleEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAlmacen());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTransferenciaMultiple());
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
		regresar.put("idSolicito", getIdSolicito());
		regresar.put("idTransferenciaMultipleEstatus", getIdTransferenciaMultipleEstatus());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idAlmacen", getIdAlmacen());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("orden", getOrden());
		regresar.put("idTransferenciaMultiple", getIdTransferenciaMultiple());
		regresar.put("ejercicio", getEjercicio());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getConsecutivo(), getIdSolicito(), getIdTransferenciaMultipleEstatus(), getIdUsuario(), getIdAlmacen(), getObservaciones(), getIdEmpresa(), getOrden(), getIdTransferenciaMultiple(), getEjercicio(), getRegistro()
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
    regresar.append("idTransferenciaMultiple~");
    regresar.append(getIdTransferenciaMultiple());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdTransferenciaMultiple());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticTransferenciasMultiplesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdTransferenciaMultiple()!= null && getIdTransferenciaMultiple()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticTransferenciasMultiplesDto other = (TcManticTransferenciasMultiplesDto) obj;
    if (getIdTransferenciaMultiple() != other.idTransferenciaMultiple && (getIdTransferenciaMultiple() == null || !getIdTransferenciaMultiple().equals(other.idTransferenciaMultiple))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdTransferenciaMultiple() != null ? getIdTransferenciaMultiple().hashCode() : 0);
    return hash;
  }

}


