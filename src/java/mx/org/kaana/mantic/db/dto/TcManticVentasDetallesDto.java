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
  @Column (name="descripcion")
  private String descripcion;
  @Column (name="descuentos")
  private String descuentos;
  @Column (name="precio")
  private Double precio;
  @Column (name="codigo")
  private String codigo;
  @Column (name="impuesto")
  private Double impuesto;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_venta_detalle")
  private Long idVentaDetalle;
  @Column (name="sat")
  private String sat;
  @Column (name="cantidad")
  private Long cantidad;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="id_venta")
  private Long idVenta;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticVentasDetallesDto() {
    this(new Long(-1L));
  }

  public TcManticVentasDetallesDto(Long key) {
    this(null, null, null, null, null, new Long(-1L), null, null, null, null);
    setKey(key);
  }

  public TcManticVentasDetallesDto(String descripcion, String descuentos, Double precio, String codigo, Double impuesto, Long idVentaDetalle, String sat, Long cantidad, Long idArticulo, Long idVenta) {
    setDescripcion(descripcion);
    setDescuentos(descuentos);
    setPrecio(precio);
    setCodigo(codigo);
    setImpuesto(impuesto);
    setIdVentaDetalle(idVentaDetalle);
    setSat(sat);
    setCantidad(cantidad);
    setIdArticulo(idArticulo);
    setIdVenta(idVenta);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescuentos(String descuentos) {
    this.descuentos = descuentos;
  }

  public String getDescuentos() {
    return descuentos;
  }

  public void setPrecio(Double precio) {
    this.precio = precio;
  }

  public Double getPrecio() {
    return precio;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setImpuesto(Double impuesto) {
    this.impuesto = impuesto;
  }

  public Double getImpuesto() {
    return impuesto;
  }

  public void setIdVentaDetalle(Long idVentaDetalle) {
    this.idVentaDetalle = idVentaDetalle;
  }

  public Long getIdVentaDetalle() {
    return idVentaDetalle;
  }

  public void setSat(String sat) {
    this.sat = sat;
  }

  public String getSat() {
    return sat;
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

  public void setIdVenta(Long idVenta) {
    this.idVenta = idVenta;
  }

  public Long getIdVenta() {
    return idVenta;
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
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuentos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPrecio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCodigo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImpuesto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVentaDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSat());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVenta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("descripcion", getDescripcion());
		regresar.put("descuentos", getDescuentos());
		regresar.put("precio", getPrecio());
		regresar.put("codigo", getCodigo());
		regresar.put("impuesto", getImpuesto());
		regresar.put("idVentaDetalle", getIdVentaDetalle());
		regresar.put("sat", getSat());
		regresar.put("cantidad", getCantidad());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("idVenta", getIdVenta());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getDescripcion(), getDescuentos(), getPrecio(), getCodigo(), getImpuesto(), getIdVentaDetalle(), getSat(), getCantidad(), getIdArticulo(), getIdVenta(), getRegistro()
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


