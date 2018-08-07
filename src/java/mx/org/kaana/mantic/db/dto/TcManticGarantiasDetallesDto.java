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
@Table(name="tc_mantic_garantias_detalles")
public class TcManticGarantiasDetallesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_garantia")
  private Long idGarantia;
  @Column (name="descuentos")
  private Double descuentos;
  @Column (name="id_proveedor")
  private Long idProveedor;
  @Column (name="descuento")
  private String descuento;
  @Column (name="extras")
  private String extras;
  @Column (name="utilidad")
  private Double utilidad;
  @Column (name="importe")
  private Double importe;
  @Column (name="registro")
  private Timestamp registro;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_garantia_detalle")
  private Long idGarantiaDetalle;
  @Column (name="id_venta_detalle")
  private Long idVentaDetalle;
  @Column (name="iva")
  private Double iva;
  @Column (name="impuestos")
  private Double impuestos;
  @Column (name="sub_total")
  private Double subTotal;
  @Column (name="cantidad")
  private Double cantidad;
  @Column (name="id_reparacion")
  private Long idReparacion;

  public TcManticGarantiasDetallesDto() {
    this(new Long(-1L));
  }

  public TcManticGarantiasDetallesDto(Long key) {
    this(null, null, null, null, null, null, null, new Long(-1L), null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticGarantiasDetallesDto(Long idGarantia, Double descuentos, Long idProveedor, String descuento, String extras, Double utilidad, Double importe, Long idGarantiaDetalle, Long idVentaDetalle, Double iva, Double impuestos, Double subTotal, Double cantidad, Long idReparacion) {
    setIdGarantia(idGarantia);
    setDescuentos(descuentos);
    setIdProveedor(idProveedor);
    setDescuento(descuento);
    setExtras(extras);
    setUtilidad(utilidad);
    setImporte(importe);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setIdGarantiaDetalle(idGarantiaDetalle);
    setIdVentaDetalle(idVentaDetalle);
    setIva(iva);
    setImpuestos(impuestos);
    setSubTotal(subTotal);
    setCantidad(cantidad);
    setIdReparacion(idReparacion);
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

  public void setIdProveedor(Long idProveedor) {
    this.idProveedor = idProveedor;
  }

  public Long getIdProveedor() {
    return idProveedor;
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

  public void setUtilidad(Double utilidad) {
    this.utilidad = utilidad;
  }

  public Double getUtilidad() {
    return utilidad;
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

  public void setIdGarantiaDetalle(Long idGarantiaDetalle) {
    this.idGarantiaDetalle = idGarantiaDetalle;
  }

  public Long getIdGarantiaDetalle() {
    return idGarantiaDetalle;
  }

  public void setIdVentaDetalle(Long idVentaDetalle) {
    this.idVentaDetalle = idVentaDetalle;
  }

  public Long getIdVentaDetalle() {
    return idVentaDetalle;
  }

  public void setIva(Double iva) {
    this.iva = iva;
  }

  public Double getIva() {
    return iva;
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

  public void setCantidad(Double cantidad) {
    this.cantidad = cantidad;
  }

  public Double getCantidad() {
    return cantidad;
  }

  public void setIdReparacion(Long idReparacion) {
    this.idReparacion = idReparacion;
  }

  public Long getIdReparacion() {
    return idReparacion;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdGarantiaDetalle();
  }

  @Override
  public void setKey(Long key) {
  	this.idGarantiaDetalle = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdGarantia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuentos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdProveedor());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getExtras());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getUtilidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporte());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdGarantiaDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVentaDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIva());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImpuestos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSubTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdReparacion());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idGarantia", getIdGarantia());
		regresar.put("descuentos", getDescuentos());
		regresar.put("idProveedor", getIdProveedor());
		regresar.put("descuento", getDescuento());
		regresar.put("extras", getExtras());
		regresar.put("utilidad", getUtilidad());
		regresar.put("importe", getImporte());
		regresar.put("registro", getRegistro());
		regresar.put("idGarantiaDetalle", getIdGarantiaDetalle());
		regresar.put("idVentaDetalle", getIdVentaDetalle());
		regresar.put("iva", getIva());
		regresar.put("impuestos", getImpuestos());
		regresar.put("subTotal", getSubTotal());
		regresar.put("cantidad", getCantidad());
		regresar.put("idReparacion", getIdReparacion());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdGarantia(), getDescuentos(), getIdProveedor(), getDescuento(), getExtras(), getUtilidad(), getImporte(), getRegistro(), getIdGarantiaDetalle(), getIdVentaDetalle(), getIva(), getImpuestos(), getSubTotal(), getCantidad(), getIdReparacion()
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
    regresar.append("idGarantiaDetalle~");
    regresar.append(getIdGarantiaDetalle());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdGarantiaDetalle());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticGarantiasDetallesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdGarantiaDetalle()!= null && getIdGarantiaDetalle()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticGarantiasDetallesDto other = (TcManticGarantiasDetallesDto) obj;
    if (getIdGarantiaDetalle() != other.idGarantiaDetalle && (getIdGarantiaDetalle() == null || !getIdGarantiaDetalle().equals(other.idGarantiaDetalle))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdGarantiaDetalle() != null ? getIdGarantiaDetalle().hashCode() : 0);
    return hash;
  }

}


