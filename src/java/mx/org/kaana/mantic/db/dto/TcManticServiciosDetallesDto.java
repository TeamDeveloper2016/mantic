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
@Table(name="tc_mantic_servicios_detalles")
public class TcManticServiciosDetallesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="descuentos")
  private Double descuentos;
  @Column (name="costo")
  private Double costo;
  @Column (name="iva")
  private Double iva;
  @Column (name="descuento")
  private String descuento;
  @Column (name="impuestos")
  private Double impuestos;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_servicio_detalle")
  private Long idServicioDetalle;
  @Column (name="concepto")
  private String concepto;
  @Column (name="sub_total")
  private Double subTotal;
  @Column (name="cantidad")
  private Long cantidad;
  @Column (name="id_servicio")
  private Long idServicio;
  @Column (name="importe")
  private Double importe;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticServiciosDetallesDto() {
    this(new Long(-1L));
  }

  public TcManticServiciosDetallesDto(Long key) {
    this(null, null, null, null, null, new Long(-1L), null, null, null, null, null);
    setKey(key);
  }

  public TcManticServiciosDetallesDto(Double descuentos, Double costo, Double iva, String descuento, Double impuestos, Long idServicioDetalle, String concepto, Double subTotal, Long cantidad, Long idServicio, Double importe) {
    setDescuentos(descuentos);
    setCosto(costo);
    setIva(iva);
    setDescuento(descuento);
    setImpuestos(impuestos);
    setIdServicioDetalle(idServicioDetalle);
    setConcepto(concepto);
    setSubTotal(subTotal);
    setCantidad(cantidad);
    setIdServicio(idServicio);
    setImporte(importe);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setDescuentos(Double descuentos) {
    this.descuentos = descuentos;
  }

  public Double getDescuentos() {
    return descuentos;
  }

  public void setCosto(Double costo) {
    this.costo = costo;
  }

  public Double getCosto() {
    return costo;
  }

  public void setIva(Double iva) {
    this.iva = iva;
  }

  public Double getIva() {
    return iva;
  }

  public void setDescuento(String descuento) {
    this.descuento = descuento;
  }

  public String getDescuento() {
    return descuento;
  }

  public void setImpuestos(Double impuestos) {
    this.impuestos = impuestos;
  }

  public Double getImpuestos() {
    return impuestos;
  }

  public void setIdServicioDetalle(Long idServicioDetalle) {
    this.idServicioDetalle = idServicioDetalle;
  }

  public Long getIdServicioDetalle() {
    return idServicioDetalle;
  }

  public void setConcepto(String concepto) {
    this.concepto = concepto;
  }

  public String getConcepto() {
    return concepto;
  }

  public void setSubTotal(Double subTotal) {
    this.subTotal = subTotal;
  }

  public Double getSubTotal() {
    return subTotal;
  }

  public void setCantidad(Long cantidad) {
    this.cantidad = cantidad;
  }

  public Long getCantidad() {
    return cantidad;
  }

  public void setIdServicio(Long idServicio) {
    this.idServicio = idServicio;
  }

  public Long getIdServicio() {
    return idServicio;
  }

  public void setImporte(Double importe) {
    this.importe = importe;
  }

  public Double getImporte() {
    return importe;
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
  	return getIdServicioDetalle();
  }

  @Override
  public void setKey(Long key) {
  	this.idServicioDetalle = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDescuentos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCosto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIva());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImpuestos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdServicioDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getConcepto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSubTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdServicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporte());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("descuentos", getDescuentos());
		regresar.put("costo", getCosto());
		regresar.put("iva", getIva());
		regresar.put("descuento", getDescuento());
		regresar.put("impuestos", getImpuestos());
		regresar.put("idServicioDetalle", getIdServicioDetalle());
		regresar.put("concepto", getConcepto());
		regresar.put("subTotal", getSubTotal());
		regresar.put("cantidad", getCantidad());
		regresar.put("idServicio", getIdServicio());
		regresar.put("importe", getImporte());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getDescuentos(), getCosto(), getIva(), getDescuento(), getImpuestos(), getIdServicioDetalle(), getConcepto(), getSubTotal(), getCantidad(), getIdServicio(), getImporte(), getRegistro()
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
    regresar.append("idServicioDetalle~");
    regresar.append(getIdServicioDetalle());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdServicioDetalle());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticServiciosDetallesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdServicioDetalle()!= null && getIdServicioDetalle()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticServiciosDetallesDto other = (TcManticServiciosDetallesDto) obj;
    if (getIdServicioDetalle() != other.idServicioDetalle && (getIdServicioDetalle() == null || !getIdServicioDetalle().equals(other.idServicioDetalle))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdServicioDetalle() != null ? getIdServicioDetalle().hashCode() : 0);
    return hash;
  }

}


