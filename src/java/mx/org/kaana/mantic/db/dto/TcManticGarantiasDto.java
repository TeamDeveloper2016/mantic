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
@Table(name="tc_mantic_garantias")
public class TcManticGarantiasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_garantia")
  private Long idGarantia;
  @Column (name="descuentos")
  private Double descuentos;  
  @Column (name="utilidad")
  private Double utilidad;
  @Column (name="ejercicio")
  private Long ejercicio;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="consecutivo")
  private String consecutivo;
  @Column (name="total")
  private Double total;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="impuestos")
  private Double impuestos;
  @Column (name="sub_total")
  private Double subTotal;
  @Column (name="observaciones")
  private String observaciones;  
  @Column (name="id_garantia_estatus")
  private Long idGarantiaEstatus;
  @Column (name="orden")
  private Long orden;
  @Column (name="id_venta")
  private Long idVenta;
  @Column (name="id_efectivo")
  private Long idEfectivo;

  public TcManticGarantiasDto() {
    this(new Long(-1L));
  }

  public TcManticGarantiasDto(Long key) {
    this(new Long(-1L), null, null, null, null, null, null, null, null, null, null, null, null, 1L);
    setKey(key);
  }

  public TcManticGarantiasDto(Long idGarantia, Double descuentos, Double utilidad, Long ejercicio, String consecutivo, Double total, Long idUsuario, Double impuestos, Double subTotal, String observaciones, Long idGarantiaEstatus, Long orden, Long idVenta, Long idEfectivo) {
    setIdGarantia(idGarantia);
    setDescuentos(descuentos);
    setUtilidad(utilidad);
    setEjercicio(ejercicio);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setConsecutivo(consecutivo);
    setTotal(total);
    setIdUsuario(idUsuario);
    setImpuestos(impuestos);
    setSubTotal(subTotal);
    setObservaciones(observaciones);
    setIdGarantiaEstatus(idGarantiaEstatus);
    setOrden(orden);
    setIdVenta(idVenta);
		this.idEfectivo= idEfectivo;
  }
	
  public void setIdGarantia(Long idGarantia) {
    this.idGarantia = idGarantia;
  }

  public Long getIdGarantia() {
    return idGarantia;
  }

  public void setDescuentos(Double descuentos) {
    this.descuentos = descuentos;
  }

  public Double getDescuentos() {
    return descuentos;
	}

  public void setUtilidad(Double utilidad) {
    this.utilidad = utilidad;
  }

  public Double getUtilidad() {
    return utilidad;
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

  public void setTotal(Double total) {
    this.total = total;
  }

  public Double getTotal() {
    return total;
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

  public void setIdGarantiaEstatus(Long idGarantiaEstatus) {
    this.idGarantiaEstatus = idGarantiaEstatus;
  }

  public Long getIdGarantiaEstatus() {
    return idGarantiaEstatus;
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

	public Long getIdEfectivo() {
		return idEfectivo;
	}

	public void setIdEfectivo(Long idEfectivo) {
		this.idEfectivo=idEfectivo;
	}

  @Transient
  @Override
  public Long getKey() {
  	return getIdGarantia();
  }

  @Override
  public void setKey(Long key) {
  	this.idGarantia = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdGarantia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuentos());		
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getUtilidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEjercicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImpuestos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSubTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdGarantiaEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVenta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEfectivo());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idGarantia", getIdGarantia());
		regresar.put("descuentos", getDescuentos());
		regresar.put("utilidad", getUtilidad());
		regresar.put("ejercicio", getEjercicio());
		regresar.put("registro", getRegistro());
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("total", getTotal());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("impuestos", getImpuestos());
		regresar.put("subTotal", getSubTotal());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idGarantiaEstatus", getIdGarantiaEstatus());
		regresar.put("orden", getOrden());
		regresar.put("idVenta", getIdVenta());
		regresar.put("idEfectivo", getIdEfectivo());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdGarantia(), getDescuentos(), getUtilidad(), getEjercicio(), getRegistro(), getConsecutivo(), getTotal(), getIdUsuario(), getImpuestos(), getSubTotal(), getObservaciones(), getIdGarantiaEstatus(), getOrden(), getIdVenta(), getIdEfectivo()
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
    regresar.append("idGarantia~");
    regresar.append(getIdGarantia());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdGarantia());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticGarantiasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdGarantia()!= null && getIdGarantia()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticGarantiasDto other = (TcManticGarantiasDto) obj;
    if (getIdGarantia() != other.idGarantia && (getIdGarantia() == null || !getIdGarantia().equals(other.idGarantia))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdGarantia() != null ? getIdGarantia().hashCode() : 0);
    return hash;
  }

}


