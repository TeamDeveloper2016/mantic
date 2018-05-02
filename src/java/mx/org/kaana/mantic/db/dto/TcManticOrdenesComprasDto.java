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
  private Double descuento;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_orden_compra")
  private Long idOrdenCompra;
  @Column (name="registro")
  private Timestamp registro;
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
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="referencia")
  private String referencia;
  @Column (name="id_estatus")
  private Long idEstatus;
  @Column (name="total_impuestos")
  private Double totalImpuestos;

  public TcManticOrdenesComprasDto() {
    this(new Long(-1L));
  }

  public TcManticOrdenesComprasDto(Long key) {
    this(null, null, null, null, new Long(-1L), null, null, null, new Timestamp(Calendar.getInstance().getTimeInMillis()), null, null, null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticOrdenesComprasDto(Long idProveedorPago, Long idProveedor, Long idCliente, Double descuento, Long idOrdenCompra, Long idTipoCompra, Long idGasto, Double total, Timestamp entregaEstimada, Long idUsuario, Long idAlmacen, Double subtotal, String observaciones, Long idEmpresa, String referencia, Long idEstatus, Double totalImpuestos) {
    setIdProveedorPago(idProveedorPago);
    setIdProveedor(idProveedor);
    setIdCliente(idCliente);
    setDescuento(descuento);
    setIdOrdenCompra(idOrdenCompra);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setIdTipoCompra(idTipoCompra);
    setIdGasto(idGasto);
    setTotal(total);
    setEntregaEstimada(entregaEstimada);
    setIdUsuario(idUsuario);
    setIdAlmacen(idAlmacen);
    setSubtotal(subtotal);
    setObservaciones(observaciones);
    setIdEmpresa(idEmpresa);
    setReferencia(referencia);
    setIdEstatus(idEstatus);
    setTotalImpuestos(totalImpuestos);
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

  public void setDescuento(Double descuento) {
    this.descuento = descuento;
  }

  public Double getDescuento() {
    return descuento;
  }

  public void setIdOrdenCompra(Long idOrdenCompra) {
    this.idOrdenCompra = idOrdenCompra;
  }

  public Long getIdOrdenCompra() {
    return idOrdenCompra;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
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

  public void setReferencia(String referencia) {
    this.referencia = referencia;
  }

  public String getReferencia() {
    return referencia;
  }

  public void setIdEstatus(Long idEstatus) {
    this.idEstatus = idEstatus;
  }

  public Long getIdEstatus() {
    return idEstatus;
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
		regresar.append(getRegistro());
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
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getReferencia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTotalImpuestos());
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
		regresar.put("registro", getRegistro());
		regresar.put("idTipoCompra", getIdTipoCompra());
		regresar.put("idGasto", getIdGasto());
		regresar.put("total", getTotal());
		regresar.put("entregaEstimada", getEntregaEstimada());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idAlmacen", getIdAlmacen());
		regresar.put("subtotal", getSubtotal());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("referencia", getReferencia());
		regresar.put("idEstatus", getIdEstatus());
		regresar.put("totalImpuestos", getTotalImpuestos());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdProveedorPago(), getIdProveedor(), getIdCliente(), getDescuento(), getIdOrdenCompra(), getRegistro(), getIdTipoCompra(), getIdGasto(), getTotal(), getEntregaEstimada(), getIdUsuario(), getIdAlmacen(), getSubtotal(), getObservaciones(), getIdEmpresa(), getReferencia(), getIdEstatus(), getTotalImpuestos()
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


