package mx.org.kaana.mantic.db.dto;

import java.io.Serializable;
import java.sql.Date;
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
@Table(name="tc_mantic_ordenes_compras")
public class TcManticOrdenesComprasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_proveedor_pago")
  private Long idProveedorPago;
  @Column (name="id_proveedor")
  private Long idProveedor;
  @Column (name="id_cliente")
  private Long idCliente;
  @Column (name="descuento")
  private String descuento;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_orden_compra")
  private Long idOrdenCompra;
  @Column (name="extras")
  private String extras;
  @Column (name="ejercicio")
  private Long ejercicio;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="consecutivo")
  private String consecutivo;
  @Column (name="id_gasto")
  private Long idGasto;
  @Column (name="total")
  private Double total;
  @Column (name="entrega_estimada")
  private Date entregaEstimada;
  @Column (name="id_compra_estatus")
  private Long idCompraEstatus;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_almacen")
  private Long idAlmacen;
  @Column (name="impuestos")
  private Double impuestos;
  @Column (name="subtotal")
  private Double subtotal;
  @Column (name="tipo_de_cambio")
  private Double tipoDeCambio;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="sin_iva")
  private Long sinIva;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="orden")
  private Long orden;
  @Column (name="total_descuentos")
  private Double totalDescuentos;

  public TcManticOrdenesComprasDto() {
    this(new Long(-1L));
  }

  public TcManticOrdenesComprasDto(Long key) {
    this(null, null, null, null, new Long(-1L), null, null, null, null, null, new Date(Calendar.getInstance().getTimeInMillis()), null, null, null, null, null, null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticOrdenesComprasDto(Long idProveedorPago, Long idProveedor, Long idCliente, String descuento, Long idOrdenCompra, String extras, Long ejercicio, String consecutivo, Long idGasto, Double total, Date entregaEstimada, Long idCompraEstatus, Long idUsuario, Long idAlmacen, Double impuestos, Double subtotal, Double tipoDeCambio, String observaciones, Long sinIva, Long idEmpresa, Long orden, Double totalDescuentos) {
    setIdProveedorPago(idProveedorPago);
    setIdProveedor(idProveedor);
    setIdCliente(idCliente);
    setDescuento(descuento);
    setIdOrdenCompra(idOrdenCompra);
    setExtras(extras);
    setEjercicio(ejercicio);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setConsecutivo(consecutivo);
    setIdGasto(idGasto);
    setTotal(total);
    setEntregaEstimada(entregaEstimada);
    setIdCompraEstatus(idCompraEstatus);
    setIdUsuario(idUsuario);
    setIdAlmacen(idAlmacen);
    setImpuestos(impuestos);
    setSubtotal(subtotal);
    setTipoDeCambio(tipoDeCambio);
    setObservaciones(observaciones);
    setSinIva(sinIva);
    setIdEmpresa(idEmpresa);
    setOrden(orden);
    setTotalDescuentos(totalDescuentos);
  }
	
  public void setIdProveedorPago(Long idProveedorPago) {
    this.idProveedorPago = idProveedorPago;
  }

  public Long getIdProveedorPago() {
    return idProveedorPago;
  }

  public void setIdProveedor(Long idProveedor) {
    this.idProveedor = idProveedor;
  }

  public Long getIdProveedor() {
    return idProveedor;
  }

  public void setIdCliente(Long idCliente) {
    this.idCliente = idCliente;
  }

  public Long getIdCliente() {
    return idCliente;
  }

  public void setDescuento(String descuento) {
    this.descuento = descuento;
  }

  public String getDescuento() {
    return descuento;
  }

  public void setIdOrdenCompra(Long idOrdenCompra) {
    this.idOrdenCompra = idOrdenCompra;
  }

  public Long getIdOrdenCompra() {
    return idOrdenCompra;
  }

  public void setExtras(String extras) {
    this.extras = extras;
  }

  public String getExtras() {
    return extras;
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

  public void setIdGasto(Long idGasto) {
    this.idGasto = idGasto;
  }

  public Long getIdGasto() {
    return idGasto;
  }

  public void setTotal(Double total) {
    this.total = total;
  }

  public Double getTotal() {
    return total;
  }

  public void setEntregaEstimada(Date entregaEstimada) {
    this.entregaEstimada = entregaEstimada;
  }

  public Date getEntregaEstimada() {
    return entregaEstimada;
  }

  public void setIdCompraEstatus(Long idCompraEstatus) {
    this.idCompraEstatus = idCompraEstatus;
  }

  public Long getIdCompraEstatus() {
    return idCompraEstatus;
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

  public void setImpuestos(Double impuestos) {
    this.impuestos = impuestos;
  }

  public Double getImpuestos() {
    return impuestos;
  }

  public void setSubtotal(Double subtotal) {
    this.subtotal = subtotal;
  }

  public Double getSubtotal() {
    return subtotal;
  }

  public void setTipoDeCambio(Double tipoDeCambio) {
    this.tipoDeCambio = tipoDeCambio;
  }

  public Double getTipoDeCambio() {
    return tipoDeCambio;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setSinIva(Long sinIva) {
    this.sinIva = sinIva;
  }

  public Long getSinIva() {
    return sinIva;
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

  public void setTotalDescuentos(Double totalDescuentos) {
    this.totalDescuentos = totalDescuentos;
  }

  public Double getTotalDescuentos() {
    return totalDescuentos;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdOrdenCompra();
  }

  @Override
  public void setKey(Long key) {
  	this.idOrdenCompra = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdProveedorPago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdProveedor());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCliente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdOrdenCompra());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getExtras());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEjercicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdGasto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEntregaEstimada());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCompraEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAlmacen());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImpuestos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSubtotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTipoDeCambio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSinIva());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTotalDescuentos());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idProveedorPago", getIdProveedorPago());
		regresar.put("idProveedor", getIdProveedor());
		regresar.put("idCliente", getIdCliente());
		regresar.put("descuento", getDescuento());
		regresar.put("idOrdenCompra", getIdOrdenCompra());
		regresar.put("extras", getExtras());
		regresar.put("ejercicio", getEjercicio());
		regresar.put("registro", getRegistro());
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("idGasto", getIdGasto());
		regresar.put("total", getTotal());
		regresar.put("entregaEstimada", getEntregaEstimada());
		regresar.put("idCompraEstatus", getIdCompraEstatus());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idAlmacen", getIdAlmacen());
		regresar.put("impuestos", getImpuestos());
		regresar.put("subtotal", getSubtotal());
		regresar.put("tipoDeCambio", getTipoDeCambio());
		regresar.put("observaciones", getObservaciones());
		regresar.put("sinIva", getSinIva());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("orden", getOrden());
		regresar.put("totalDescuentos", getTotalDescuentos());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdProveedorPago(), getIdProveedor(), getIdCliente(), getDescuento(), getIdOrdenCompra(), getExtras(), getEjercicio(), getRegistro(), getConsecutivo(), getIdGasto(), getTotal(), getEntregaEstimada(), getIdCompraEstatus(), getIdUsuario(), getIdAlmacen(), getImpuestos(), getSubtotal(), getTipoDeCambio(), getObservaciones(), getSinIva(), getIdEmpresa(), getOrden(), getTotalDescuentos()
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
    regresar.append("idOrdenCompra~");
    regresar.append(getIdOrdenCompra());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdOrdenCompra());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticOrdenesComprasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdOrdenCompra()!= null && getIdOrdenCompra()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticOrdenesComprasDto other = (TcManticOrdenesComprasDto) obj;
    if (getIdOrdenCompra() != other.idOrdenCompra && (getIdOrdenCompra() == null || !getIdOrdenCompra().equals(other.idOrdenCompra))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdOrdenCompra() != null ? getIdOrdenCompra().hashCode() : 0);
    return hash;
  }

}


