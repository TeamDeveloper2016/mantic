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
@Table(name="tc_mantic_ventas_detalles")
public class TcManticVentasDetallesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="codigo")
  private String codigo;
  @Column (name="unidad_medida")
  private String unidadMedida;
  @Column (name="costo")
  private Double costo;
  @Column (name="descuento")
  private String descuento;
  @Column (name="sat")
  private String sat;
  @Column (name="extras")
  private String extras;
  @Column (name="nombre")
  private String nombre;
  @Column (name="importe")
  private Double importe;
  @Column (name="registro")
  private Timestamp registro;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_venta_detalle")
  private Long idVentaDetalle;
  @Column (name="iva")
  private Double iva;
  @Column (name="sub_total")
  private Double subTotal;
  @Column (name="cantidad")
  private Long cantidad;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="total_descuentos")
  private Double totalDescuentos;
  @Column (name="id_venta")
  private Long idVenta;
  @Column (name="total_impuestos")
  private Double totalImpuestos;

  public TcManticVentasDetallesDto() {
    this(new Long(-1L));
  }

  public TcManticVentasDetallesDto(Long key) {
    this(null, null, null, null, null, null, null, null, new Long(-1L), null, null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticVentasDetallesDto(String codigo, String unidadMedida, Double costo, String descuento, String sat, String extras, String nombre, Double importe, Long idVentaDetalle, Double iva, Double subTotal, Long cantidad, Long idArticulo, Double totalDescuentos, Long idVenta, Double totalImpuestos) {
    setCodigo(codigo);
    setUnidadMedida(unidadMedida);
    setCosto(costo);
    setDescuento(descuento);
    setSat(sat);
    setExtras(extras);
    setNombre(nombre);
    setImporte(importe);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setIdVentaDetalle(idVentaDetalle);
    setIva(iva);
    setSubTotal(subTotal);
    setCantidad(cantidad);
    setIdArticulo(idArticulo);
    setTotalDescuentos(totalDescuentos);
    setIdVenta(idVenta);
    setTotalImpuestos(totalImpuestos);
  }
	
  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setUnidadMedida(String unidadMedida) {
    this.unidadMedida = unidadMedida;
  }

  public String getUnidadMedida() {
    return unidadMedida;
  }

  public void setCosto(Double costo) {
    this.costo = costo;
  }

  public Double getCosto() {
    return costo;
  }

  public void setDescuento(String descuento) {
    this.descuento = descuento;
  }

  public String getDescuento() {
    return descuento;
  }

  public void setSat(String sat) {
    this.sat = sat;
  }

  public String getSat() {
    return sat;
  }

  public void setExtras(String extras) {
    this.extras = extras;
  }

  public String getExtras() {
    return extras;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
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

  public void setIdArticulo(Long idArticulo) {
    this.idArticulo = idArticulo;
  }

  public Long getIdArticulo() {
    return idArticulo;
  }

  public void setTotalDescuentos(Double totalDescuentos) {
    this.totalDescuentos = totalDescuentos;
  }

  public Double getTotalDescuentos() {
    return totalDescuentos;
  }

  public void setIdVenta(Long idVenta) {
    this.idVenta = idVenta;
  }

  public Long getIdVenta() {
    return idVenta;
  }

  public void setTotalImpuestos(Double totalImpuestos) {
    this.totalImpuestos = totalImpuestos;
  }

  public Double getTotalImpuestos() {
    return totalImpuestos;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdVentaDetalle();
  }

  @Override
  public void setKey(Long key) {
  	this.idVentaDetalle = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getCodigo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getUnidadMedida());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCosto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSat());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getExtras());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporte());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVentaDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIva());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSubTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTotalDescuentos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVenta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTotalImpuestos());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("codigo", getCodigo());
		regresar.put("unidadMedida", getUnidadMedida());
		regresar.put("costo", getCosto());
		regresar.put("descuento", getDescuento());
		regresar.put("sat", getSat());
		regresar.put("extras", getExtras());
		regresar.put("nombre", getNombre());
		regresar.put("importe", getImporte());
		regresar.put("registro", getRegistro());
		regresar.put("idVentaDetalle", getIdVentaDetalle());
		regresar.put("iva", getIva());
		regresar.put("subTotal", getSubTotal());
		regresar.put("cantidad", getCantidad());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("totalDescuentos", getTotalDescuentos());
		regresar.put("idVenta", getIdVenta());
		regresar.put("totalImpuestos", getTotalImpuestos());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getCodigo(), getUnidadMedida(), getCosto(), getDescuento(), getSat(), getExtras(), getNombre(), getImporte(), getRegistro(), getIdVentaDetalle(), getIva(), getSubTotal(), getCantidad(), getIdArticulo(), getTotalDescuentos(), getIdVenta(), getTotalImpuestos()
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
    regresar.append("idVentaDetalle~");
    regresar.append(getIdVentaDetalle());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdVentaDetalle());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticVentasDetallesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdVentaDetalle()!= null && getIdVentaDetalle()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticVentasDetallesDto other = (TcManticVentasDetallesDto) obj;
    if (getIdVentaDetalle() != other.idVentaDetalle && (getIdVentaDetalle() == null || !getIdVentaDetalle().equals(other.idVentaDetalle))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdVentaDetalle() != null ? getIdVentaDetalle().hashCode() : 0);
    return hash;
  }

}


