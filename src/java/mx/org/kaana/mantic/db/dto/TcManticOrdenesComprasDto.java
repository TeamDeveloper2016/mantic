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
@Table(name="tc_mantic_ordenes_compras")
public class TcManticOrdenesComprasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_proveedor_pago")
  private Long idProveedorPago;
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
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="consecutivo")
  private String consecutivo;
  @Column (name="id_tipo_compra")
  private Long idTipoCompra;
  @Column (name="id_gasto")
  private Long idGasto;
  @Column (name="total")
  private Double total;
  @Column (name="entrega_estimada")
  private Timestamp entregaEstimada;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_almacen")
  private Long idAlmacen;
  @Column (name="subtotal")
  private Double subtotal;
  @Column (name="impuestos")
  private Double impuestos;
  @Column (name="tipo_de_cambio")
  private Double tipoDeCambio;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="id_estatus")
  private Long idEstatus;

  public TcManticOrdenesComprasDto() {
    this(new Long(-1L));
  }

  public TcManticOrdenesComprasDto(Long key) {
    this(null, null, null, new Long(-1L), null, null, null, null, null, new Timestamp(Calendar.getInstance().getTimeInMillis()), null, null, null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticOrdenesComprasDto(Long idProveedorPago, Long idCliente, String descuento, Long idOrdenCompra, String extras, String consecutivo, Long idTipoCompra, Long idGasto, Double total, Timestamp entregaEstimada, Long idUsuario, Long idAlmacen, Double subtotal, Double impuestos, Double tipoDeCambio, String observaciones, Long idEmpresa, Long idEstatus) {
    setIdProveedorPago(idProveedorPago);
    setIdCliente(idCliente);
    setDescuento(descuento);
    setIdOrdenCompra(idOrdenCompra);
    setExtras(extras);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setConsecutivo(consecutivo);
    setIdTipoCompra(idTipoCompra);
    setIdGasto(idGasto);
    setTotal(total);
    setEntregaEstimada(entregaEstimada);
    setIdUsuario(idUsuario);
    setIdAlmacen(idAlmacen);
    setSubtotal(subtotal);
    setImpuestos(impuestos);
    setTipoDeCambio(tipoDeCambio);
    setObservaciones(observaciones);
    setIdEmpresa(idEmpresa);
    setIdEstatus(idEstatus);
  }
	
  public void setIdProveedorPago(Long idProveedorPago) {
    this.idProveedorPago = idProveedorPago;
  }

  public Long getIdProveedorPago() {
    return idProveedorPago;
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

  public void setIdTipoCompra(Long idTipoCompra) {
    this.idTipoCompra = idTipoCompra;
  }

  public Long getIdTipoCompra() {
    return idTipoCompra;
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

  public void setEntregaEstimada(Timestamp entregaEstimada) {
    this.entregaEstimada = entregaEstimada;
  }

  public Timestamp getEntregaEstimada() {
    return entregaEstimada;
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

  public void setSubtotal(Double subtotal) {
    this.subtotal = subtotal;
  }

  public Double getSubtotal() {
    return subtotal;
  }

  public void setImpuestos(Double impuestos) {
    this.impuestos = impuestos;
  }

  public Double getImpuestos() {
    return impuestos;
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

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }

  public Long getIdEmpresa() {
    return idEmpresa;
  }

  public void setIdEstatus(Long idEstatus) {
    this.idEstatus = idEstatus;
  }

  public Long getIdEstatus() {
    return idEstatus;
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
		regresar.append(getIdCliente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdOrdenCompra());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getExtras());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoCompra());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdGasto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEntregaEstimada());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAlmacen());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSubtotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImpuestos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTipoDeCambio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEstatus());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idProveedorPago", getIdProveedorPago());
		regresar.put("idCliente", getIdCliente());
		regresar.put("descuento", getDescuento());
		regresar.put("idOrdenCompra", getIdOrdenCompra());
		regresar.put("extras", getExtras());
		regresar.put("registro", getRegistro());
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("idTipoCompra", getIdTipoCompra());
		regresar.put("idGasto", getIdGasto());
		regresar.put("total", getTotal());
		regresar.put("entregaEstimada", getEntregaEstimada());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idAlmacen", getIdAlmacen());
		regresar.put("subtotal", getSubtotal());
		regresar.put("impuestos", getImpuestos());
		regresar.put("tipoDeCambio", getTipoDeCambio());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("idEstatus", getIdEstatus());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdProveedorPago(), getIdCliente(), getDescuento(), getIdOrdenCompra(), getExtras(), getRegistro(), getConsecutivo(), getIdTipoCompra(), getIdGasto(), getTotal(), getEntregaEstimada(), getIdUsuario(), getIdAlmacen(), getSubtotal(), getImpuestos(), getTipoDeCambio(), getObservaciones(), getIdEmpresa(), getIdEstatus()
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


