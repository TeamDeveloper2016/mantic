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
@Table(name="tc_mantic_ventas")
public class TcManticVentasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="descuentos")
  private Double descuentos;
  @Column (name="id_factura")
  private Long idFactura;
  @Column (name="id_tipo_medio_pago")
  private Long idTipoMedioPago;
  @Column (name="id_cliente")
  private Long idCliente;
  @Column (name="descuento")
  private String descuento;
  @Column (name="extras")
  private String extras;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="consecutivo")
  private Long consecutivo;
  @Column (name="total")
  private Double total;
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
  @Column (name="id_sin_iva")
  private Long idSinIva;
  @Column (name="tipo_tarjeta")
  private String tipoTarjeta;
  @Column (name="efectivo")
  private Double efectivo;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="tarjeta")
  private String tarjeta;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_venta")
  private Long idVenta;
  @Column (name="dia")
  private Date dia;
  @Column (name="id_venta_estatus")
  private Long idVentaEstatus;

  public TcManticVentasDto() {
    this(new Long(-1L));
  }

  public TcManticVentasDto(Long key) {
    this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, new Long(-1L), new Date(Calendar.getInstance().getTimeInMillis()), null);
    setKey(key);
  }

  public TcManticVentasDto(Double descuentos, Long idFactura, Long idTipoMedioPago, Long idCliente, String descuento, String extras, Long consecutivo, Double total, Long idUsuario, Long idAlmacen, Double subtotal, Double impuestos, Double tipoDeCambio, Long idSinIva, String tipoTarjeta, Double efectivo, String observaciones, String tarjeta, Long idVenta, Date dia, Long idVentaEstatus) {
    setDescuentos(descuentos);
    setIdFactura(idFactura);
    setIdTipoMedioPago(idTipoMedioPago);
    setIdCliente(idCliente);
    setDescuento(descuento);
    setExtras(extras);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setConsecutivo(consecutivo);
    setTotal(total);
    setIdUsuario(idUsuario);
    setIdAlmacen(idAlmacen);
    setSubtotal(subtotal);
    setImpuestos(impuestos);
    setTipoDeCambio(tipoDeCambio);
    setIdSinIva(idSinIva);
    setTipoTarjeta(tipoTarjeta);
    setEfectivo(efectivo);
    setObservaciones(observaciones);
    setTarjeta(tarjeta);
    setIdVenta(idVenta);
    setDia(dia);
    setIdVentaEstatus(idVentaEstatus);
  }
	
  public void setDescuentos(Double descuentos) {
    this.descuentos = descuentos;
  }

  public Double getDescuentos() {
    return descuentos;
  }

  public void setIdFactura(Long idFactura) {
    this.idFactura = idFactura;
  }

  public Long getIdFactura() {
    return idFactura;
  }

  public void setIdTipoMedioPago(Long idTipoMedioPago) {
    this.idTipoMedioPago = idTipoMedioPago;
  }

  public Long getIdTipoMedioPago() {
    return idTipoMedioPago;
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

  public void setConsecutivo(Long consecutivo) {
    this.consecutivo = consecutivo;
  }

  public Long getConsecutivo() {
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

  public void setIdSinIva(Long idSinIva) {
    this.idSinIva = idSinIva;
  }

  public Long getIdSinIva() {
    return idSinIva;
  }

  public void setTipoTarjeta(String tipoTarjeta) {
    this.tipoTarjeta = tipoTarjeta;
  }

  public String getTipoTarjeta() {
    return tipoTarjeta;
  }

  public void setEfectivo(Double efectivo) {
    this.efectivo = efectivo;
  }

  public Double getEfectivo() {
    return efectivo;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setTarjeta(String tarjeta) {
    this.tarjeta = tarjeta;
  }

  public String getTarjeta() {
    return tarjeta;
  }

  public void setIdVenta(Long idVenta) {
    this.idVenta = idVenta;
  }

  public Long getIdVenta() {
    return idVenta;
  }

  public void setDia(Date dia) {
    this.dia = dia;
  }

  public Date getDia() {
    return dia;
  }

  public void setIdVentaEstatus(Long idVentaEstatus) {
    this.idVentaEstatus = idVentaEstatus;
  }

  public Long getIdVentaEstatus() {
    return idVentaEstatus;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdVenta();
  }

  @Override
  public void setKey(Long key) {
  	this.idVenta = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDescuentos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdFactura());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoMedioPago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCliente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getExtras());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTotal());
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
		regresar.append(getIdSinIva());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTipoTarjeta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEfectivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTarjeta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVenta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVentaEstatus());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("descuentos", getDescuentos());
		regresar.put("idFactura", getIdFactura());
		regresar.put("idTipoMedioPago", getIdTipoMedioPago());
		regresar.put("idCliente", getIdCliente());
		regresar.put("descuento", getDescuento());
		regresar.put("extras", getExtras());
		regresar.put("registro", getRegistro());
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("total", getTotal());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idAlmacen", getIdAlmacen());
		regresar.put("subtotal", getSubtotal());
		regresar.put("impuestos", getImpuestos());
		regresar.put("tipoDeCambio", getTipoDeCambio());
		regresar.put("idSinIva", getIdSinIva());
		regresar.put("tipoTarjeta", getTipoTarjeta());
		regresar.put("efectivo", getEfectivo());
		regresar.put("observaciones", getObservaciones());
		regresar.put("tarjeta", getTarjeta());
		regresar.put("idVenta", getIdVenta());
		regresar.put("dia", getDia());
		regresar.put("idVentaEstatus", getIdVentaEstatus());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getDescuentos(), getIdFactura(), getIdTipoMedioPago(), getIdCliente(), getDescuento(), getExtras(), getRegistro(), getConsecutivo(), getTotal(), getIdUsuario(), getIdAlmacen(), getSubtotal(), getImpuestos(), getTipoDeCambio(), getIdSinIva(), getTipoTarjeta(), getEfectivo(), getObservaciones(), getTarjeta(), getIdVenta(), getDia(), getIdVentaEstatus()
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
    regresar.append("idVenta~");
    regresar.append(getIdVenta());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdVenta());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticVentasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdVenta()!= null && getIdVenta()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticVentasDto other = (TcManticVentasDto) obj;
    if (getIdVenta() != other.idVenta && (getIdVenta() == null || !getIdVenta().equals(other.idVenta))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdVenta() != null ? getIdVenta().hashCode() : 0);
    return hash;
  }

}


