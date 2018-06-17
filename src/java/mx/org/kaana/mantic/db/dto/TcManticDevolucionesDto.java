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
@Table(name="tc_mantic_devoluciones")
public class TcManticDevolucionesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="descuentos")
  private Double descuentos;
  @Column (name="descuento")
  private String descuento;
  @Column (name="extras")
  private String extras;
  @Column (name="id_nota_entrada")
  private Long idNotaEntrada;
  @Column (name="ejercicio")
  private Long ejercicio;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="consecutivo")
  private String consecutivo;
  @Column (name="id_devolucion_estatus")
  private Long idDevolucionEstatus;
  @Column (name="total")
  private Double total;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_devolucion")
  private Long idDevolucion;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="impuestos")
  private Double impuestos;
  @Column (name="sub_total")
  private Double subTotal;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="orden")
  private Long orden;

  public TcManticDevolucionesDto() {
    this(new Long(-1L));
  }

  public TcManticDevolucionesDto(Long key) {
    this(null, null, null, null, null, null, null, null, new Long(-1L), null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticDevolucionesDto(Double descuentos, String descuento, String extras, Long idNotaEntrada, Long ejercicio, String consecutivo, Long idDevolucionEstatus, Double total, Long idDevolucion, Long idUsuario, Double impuestos, Double subTotal, String observaciones, Long idEmpresa, Long orden) {
    setDescuentos(descuentos);
    setDescuento(descuento);
    setExtras(extras);
    setIdNotaEntrada(idNotaEntrada);
    setEjercicio(ejercicio);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setConsecutivo(consecutivo);
    setIdDevolucionEstatus(idDevolucionEstatus);
    setTotal(total);
    setIdDevolucion(idDevolucion);
    setIdUsuario(idUsuario);
    setImpuestos(impuestos);
    setSubTotal(subTotal);
    setObservaciones(observaciones);
    setIdEmpresa(idEmpresa);
    setOrden(orden);
  }
	
  public void setDescuentos(Double descuentos) {
    this.descuentos = descuentos;
  }

  public Double getDescuentos() {
    return descuentos;
  }

  public void setDescuento(String descuento) {
    this.descuento = descuento;
  }

  public String getDescuento() {
    return descuento;
  }

  public void setExtras(String extras) {
    this.extras = extras;
  }

  public String getExtras() {
    return extras;
  }

  public void setIdNotaEntrada(Long idNotaEntrada) {
    this.idNotaEntrada = idNotaEntrada;
  }

  public Long getIdNotaEntrada() {
    return idNotaEntrada;
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

  public void setIdDevolucionEstatus(Long idDevolucionEstatus) {
    this.idDevolucionEstatus = idDevolucionEstatus;
  }

  public Long getIdDevolucionEstatus() {
    return idDevolucionEstatus;
  }

  public void setTotal(Double total) {
    this.total = total;
  }

  public Double getTotal() {
    return total;
  }

  public void setIdDevolucion(Long idDevolucion) {
    this.idDevolucion = idDevolucion;
  }

  public Long getIdDevolucion() {
    return idDevolucion;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setImpuestos(Double impuestos) {
    this.impuestos = impuestos;
  }

  public Double getImpuestos() {
    return impuestos;
  }

  public void setSubTotal(Double subTotal) {
    this.subTotal = subTotal;
  }

  public Double getSubTotal() {
    return subTotal;
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

  @Transient
  @Override
  public Long getKey() {
  	return getIdDevolucion();
  }

  @Override
  public void setKey(Long key) {
  	this.idDevolucion = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDescuentos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getExtras());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdNotaEntrada());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEjercicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdDevolucionEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdDevolucion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImpuestos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSubTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("descuentos", getDescuentos());
		regresar.put("descuento", getDescuento());
		regresar.put("extras", getExtras());
		regresar.put("idNotaEntrada", getIdNotaEntrada());
		regresar.put("ejercicio", getEjercicio());
		regresar.put("registro", getRegistro());
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("idDevolucionEstatus", getIdDevolucionEstatus());
		regresar.put("total", getTotal());
		regresar.put("idDevolucion", getIdDevolucion());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("impuestos", getImpuestos());
		regresar.put("subTotal", getSubTotal());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("orden", getOrden());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getDescuentos(), getDescuento(), getExtras(), getIdNotaEntrada(), getEjercicio(), getRegistro(), getConsecutivo(), getIdDevolucionEstatus(), getTotal(), getIdDevolucion(), getIdUsuario(), getImpuestos(), getSubTotal(), getObservaciones(), getIdEmpresa(), getOrden()
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
    regresar.append("idDevolucion~");
    regresar.append(getIdDevolucion());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdDevolucion());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticDevolucionesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdDevolucion()!= null && getIdDevolucion()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticDevolucionesDto other = (TcManticDevolucionesDto) obj;
    if (getIdDevolucion() != other.idDevolucion && (getIdDevolucion() == null || !getIdDevolucion().equals(other.idDevolucion))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdDevolucion() != null ? getIdDevolucion().hashCode() : 0);
    return hash;
  }

}


