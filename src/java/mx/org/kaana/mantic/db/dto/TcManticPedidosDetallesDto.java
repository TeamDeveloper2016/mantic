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
@Table(name="tc_mantic_pedidos_detalles")
public class TcManticPedidosDetallesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="descuentos")
  private Double descuentos;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_pedido_detalle")
  private Long idPedidoDetalle;
  @Column (name="codigo")
  private String codigo;
  @Column (name="unidad_medida")
  private String unidadMedida;
  @Column (name="descuento")
  private String descuento;
  @Column (name="sat")
  private String sat;
  @Column (name="extra")
  private String extra;
  @Column (name="nombre")
  private String nombre;
  @Column (name="importe")
  private Double importe;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="precio")
  private Double precio;
  @Column (name="iva")
  private Double iva;
  @Column (name="impuestos")
  private Double impuestos;
  @Column (name="unitario_sin_iva")
  private Double unitarioSinIva;
  @Column (name="sub_total")
  private Double subTotal;
  @Column (name="cantidad")
  private Double cantidad;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="id_pedido")
  private Long idPedido;

  public TcManticPedidosDetallesDto() {
    this(new Long(-1L));
  }

  public TcManticPedidosDetallesDto(Long key) {
    this(null, new Long(-1L), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticPedidosDetallesDto(Double descuentos, Long idPedidoDetalle, String codigo, String unidadMedida, String descuento, String sat, String extra, String nombre, Double importe, Double precio, Double iva, Double impuestos, Double unitarioSinIva, Double subTotal, Double cantidad, Long idArticulo, Long idPedido) {
    setDescuentos(descuentos);
    setIdPedidoDetalle(idPedidoDetalle);
    setCodigo(codigo);
    setUnidadMedida(unidadMedida);
    setDescuento(descuento);
    setSat(sat);
    setExtra(extra);
    setNombre(nombre);
    setImporte(importe);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setPrecio(precio);
    setIva(iva);
    setImpuestos(impuestos);
    setUnitarioSinIva(unitarioSinIva);
    setSubTotal(subTotal);
    setCantidad(cantidad);
    setIdArticulo(idArticulo);
    setIdPedido(idPedido);
  }
	
  public void setDescuentos(Double descuentos) {
    this.descuentos = descuentos;
  }

  public Double getDescuentos() {
    return descuentos;
  }

  public void setIdPedidoDetalle(Long idPedidoDetalle) {
    this.idPedidoDetalle = idPedidoDetalle;
  }

  public Long getIdPedidoDetalle() {
    return idPedidoDetalle;
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

  public void setExtra(String extra) {
    this.extra = extra;
  }

  public String getExtra() {
    return extra;
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

  public void setPrecio(Double precio) {
    this.precio = precio;
  }

  public Double getPrecio() {
    return precio;
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

  public void setUnitarioSinIva(Double unitarioSinIva) {
    this.unitarioSinIva = unitarioSinIva;
  }

  public Double getUnitarioSinIva() {
    return unitarioSinIva;
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

  public void setIdArticulo(Long idArticulo) {
    this.idArticulo = idArticulo;
  }

  public Long getIdArticulo() {
    return idArticulo;
  }

  public void setIdPedido(Long idPedido) {
    this.idPedido = idPedido;
  }

  public Long getIdPedido() {
    return idPedido;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdPedidoDetalle();
  }

  @Override
  public void setKey(Long key) {
  	this.idPedidoDetalle = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDescuentos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPedidoDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCodigo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getUnidadMedida());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSat());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getExtra());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporte());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPrecio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIva());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImpuestos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getUnitarioSinIva());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSubTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPedido());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("descuentos", getDescuentos());
		regresar.put("idPedidoDetalle", getIdPedidoDetalle());
		regresar.put("codigo", getCodigo());
		regresar.put("unidadMedida", getUnidadMedida());
		regresar.put("descuento", getDescuento());
		regresar.put("sat", getSat());
		regresar.put("extra", getExtra());
		regresar.put("nombre", getNombre());
		regresar.put("importe", getImporte());
		regresar.put("registro", getRegistro());
		regresar.put("precio", getPrecio());
		regresar.put("iva", getIva());
		regresar.put("impuestos", getImpuestos());
		regresar.put("unitarioSinIva", getUnitarioSinIva());
		regresar.put("subTotal", getSubTotal());
		regresar.put("cantidad", getCantidad());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("idPedido", getIdPedido());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getDescuentos(), getIdPedidoDetalle(), getCodigo(), getUnidadMedida(), getDescuento(), getSat(), getExtra(), getNombre(), getImporte(), getRegistro(), getPrecio(), getIva(), getImpuestos(), getUnitarioSinIva(), getSubTotal(), getCantidad(), getIdArticulo(), getIdPedido()
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
    regresar.append("idPedidoDetalle~");
    regresar.append(getIdPedidoDetalle());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdPedidoDetalle());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticPedidosDetallesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdPedidoDetalle()!= null && getIdPedidoDetalle()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticPedidosDetallesDto other = (TcManticPedidosDetallesDto) obj;
    if (getIdPedidoDetalle() != other.idPedidoDetalle && (getIdPedidoDetalle() == null || !getIdPedidoDetalle().equals(other.idPedidoDetalle))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdPedidoDetalle() != null ? getIdPedidoDetalle().hashCode() : 0);
    return hash;
  }

}


