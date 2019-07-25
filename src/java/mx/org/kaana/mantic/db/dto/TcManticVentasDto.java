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
@Table(name="tc_mantic_ventas")
public class TcManticVentasDto implements IBaseDto, Serializable {

	private static final long serialVersionUID=3391576865327806322L;
	
  @Column (name="descuentos")
  private Double descuentos;
  @Column (name="id_factura")
  private Long idFactura;
  @Column (name="id_facturar")
  private Long idFacturar;
  @Column (name="id_credito")
  private Long idCredito;
  @Column (name="extras")
  private String extras;
  @Column (name="global")
  private Double global;
  @Column (name="utilidad")
  private Double utilidad;
  @Column (name="total")
  private Double total;
  @Column (name="id_almacen")
  private Long idAlmacen;
  @Column (name="tipo_de_cambio")
  private Double tipoDeCambio;
  @Column (name="orden")
  private Long orden;
  @Column (name="cticket")
  private Long cticket;
  @Column (name="ccotizacion")
  private Long ccotizacion;
  @Column (name="vigencia")
  private Date vigencia;
  @Column (name="id_autorizar")
  private Long idAutorizar;
  @Column (name="id_cliente")
  private Long idCliente;
  @Column (name="ticket")
  private String ticket;
  @Column (name="descuento")
  private String descuento;
  @Column (name="ejercicio")
  private Long ejercicio;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="cobro")
  private Timestamp cobro;
  @Column (name="consecutivo")
  private Long consecutivo;
  @Column (name="cotizacion")
  private String cotizacion;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="impuestos")
  private Double impuestos;
  @Column (name="id_uso_cfdi")
  private Long idUsoCfdi;
  @Column (name="id_sin_iva")
  private Long idSinIva;
  @Column (name="sub_total")
  private Double subTotal;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_venta")
  private Long idVenta;
  @Column (name="dia")
  private Date dia;
  @Column (name="id_venta_estatus")
  private Long idVentaEstatus;
	@Column (name="id_manual")
  private Long idManual;
	@Column (name="id_cliente_domicilio")
  private Long idClienteDomicilio;
	@Column (name="id_tipo_medio_pago")
  private Long idTipoMedioPago;
	@Column (name="id_tipo_pago")
  private Long idTipoPago;
	@Column (name="id_banco")
  private Long idBanco;
  @Column (name="referencia")
  private String referencia;
	@Column (name="id_tipo_documento")
  private Long idTipoDocumento;
	@Column (name="candado")
  private Long candado;
	@Column (name="id_sincronizado")
  private Long idSincronizado;

  public TcManticVentasDto() {
    this(new Long(-1L));
  }

  public TcManticVentasDto(Long key) {
    this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, new Long(-1L), new Date(Calendar.getInstance().getTimeInMillis()), null, null, null, null, null, new Date(Calendar.getInstance().getTimeInMillis()), 2L, null);
    setKey(key);
  }

	public TcManticVentasDto(Double descuentos, Long idFactura, Long idCredito, String extras, Double total, Long idAlmacen, Double tipoDeCambio, Long orden, Long idAutorizar, Long idCliente, String descuento, Long ejercicio, Long consecutivo, Long idUsuario, Double impuestos, Long idUsoCfdi, Long idSinIva, Double subTotal, String observaciones, Long idEmpresa, Long idVenta, Date dia, Long idVentaEstatus, Long idFacturar) {
		this(descuentos, idFactura, idCredito, extras, null, null, total, idAlmacen, tipoDeCambio, orden, idAutorizar, idCliente, descuento, ejercicio, consecutivo, idUsuario, impuestos, idUsoCfdi, idSinIva, subTotal, observaciones, idEmpresa, idVenta, dia, idVentaEstatus, null, null, null, null, new Date(Calendar.getInstance().getTimeInMillis()), 2L, idFacturar);
	}
	
  public TcManticVentasDto(Double descuentos, Long idFactura, Long idCredito, String extras, Double global, Double utilidad, Double total, Long idAlmacen, Double tipoDeCambio, Long orden, Long idAutorizar, Long idCliente, String descuento, Long ejercicio, Long consecutivo, Long idUsuario, Double impuestos, Long idUsoCfdi, Long idSinIva, Double subTotal, String observaciones, Long idEmpresa, Long idVenta, Date dia, Long idVentaEstatus, String cotizacion, String ticket, Long ccotizacion, Long cticket, Date vigencia, Long idManual, Long idFacturar){
		this(descuentos, idFactura, idCredito, extras, null, null, total, idAlmacen, tipoDeCambio, orden, idAutorizar, idCliente, descuento, ejercicio, consecutivo, idUsuario, impuestos, idUsoCfdi, idSinIva, subTotal, observaciones, idEmpresa, idVenta, dia, idVentaEstatus, null, null, null, null, new Date(Calendar.getInstance().getTimeInMillis()), 2L, idFacturar, new Timestamp(Calendar.getInstance().getTimeInMillis()));
	}
	
  public TcManticVentasDto(Double descuentos, Long idFactura, Long idCredito, String extras, Double global, Double utilidad, Double total, Long idAlmacen, Double tipoDeCambio, Long orden, Long idAutorizar, Long idCliente, String descuento, Long ejercicio, Long consecutivo, Long idUsuario, Double impuestos, Long idUsoCfdi, Long idSinIva, Double subTotal, String observaciones, Long idEmpresa, Long idVenta, Date dia, Long idVentaEstatus, String cotizacion, String ticket, Long ccotizacion, Long cticket, Date vigencia, Long idManual, Long idFacturar, Timestamp cobro) {
		this(descuentos, idFactura, idCredito, extras, global, utilidad, total, idAlmacen, tipoDeCambio, orden, idAutorizar, idCliente, descuento, ejercicio, consecutivo, idUsuario, impuestos, idUsoCfdi, idSinIva, subTotal, observaciones, idEmpresa, idVenta, dia, idVentaEstatus, cotizacion, ticket, ccotizacion, cticket, vigencia, idManual, idFacturar, cobro, null, null, null, null, null, 2L, 2L);
	}
	
  public TcManticVentasDto(Double descuentos, Long idFactura, Long idCredito, String extras, Double global, Double utilidad, Double total, Long idAlmacen, Double tipoDeCambio, Long orden, Long idAutorizar, Long idCliente, String descuento, Long ejercicio, Long consecutivo, Long idUsuario, Double impuestos, Long idUsoCfdi, Long idSinIva, Double subTotal, String observaciones, Long idEmpresa, Long idVenta, Date dia, Long idVentaEstatus, String cotizacion, String ticket, Long ccotizacion, Long cticket, Date vigencia, Long idManual, Long idFacturar, Timestamp cobro, Long idClienteDomicilio, Long idTipoMedioPago, Long idTipoPago, Long idBanco, String referencia, Long idTipoDocumento, Long candado) {
    setDescuentos(descuentos);
    setIdFactura(idFactura);
    setIdCredito(idCredito);
    setExtras(extras);
    setGlobal(global);
    setUtilidad(utilidad);
    setTotal(total);
    setIdAlmacen(idAlmacen);
    setTipoDeCambio(tipoDeCambio);
    setOrden(orden);
    setIdAutorizar(idAutorizar);
    setIdCliente(idCliente);
    setDescuento(descuento);
    setEjercicio(ejercicio);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setConsecutivo(consecutivo);
    setIdUsuario(idUsuario);
    setImpuestos(impuestos);
    setIdUsoCfdi(idUsoCfdi);
    setIdSinIva(idSinIva);
    setSubTotal(subTotal);
    setObservaciones(observaciones);
    setIdEmpresa(idEmpresa);
    setIdVenta(idVenta);
    setDia(dia);
    setIdVentaEstatus(idVentaEstatus);
		setTicket(ticket);
		setCotizacion(cotizacion);
		setCticket(cticket);
		setCcotizacion(ccotizacion);
		setVigencia(vigencia);
		setIdManual(idManual);
		this.idFacturar= idFacturar;
		setCobro(cobro);
		this.idClienteDomicilio= idClienteDomicilio;
		this.idTipoMedioPago= idTipoMedioPago;
		this.idTipoPago= idTipoPago; 
		this.idBanco= idBanco;
		this.referencia= referencia;
		this.idTipoDocumento= idTipoDocumento;
		setCandado(candado);
		this.idSincronizado= 2L;
  }
	
	public Long getIdManual() {
		return idManual;
	}

	public void setIdManual(Long idManual) {
		this.idManual = idManual;
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

	public Long getIdFacturar() {
		return idFacturar;
	}

	public void setIdFacturar(Long idFacturar) {
		this.idFacturar=idFacturar;
	}

  public void setIdCredito(Long idCredito) {
    this.idCredito = idCredito;
  }

  public Long getIdCredito() {
    return idCredito;
  }

  public void setExtras(String extras) {
    this.extras = extras;
  }

  public String getExtras() {
    return extras;
  }

  public void setGlobal(Double global) {
    this.global = global;
  }

  public Double getGlobal() {
    return global;
  }

  public void setUtilidad(Double utilidad) {
    this.utilidad = utilidad;
  }

  public Double getUtilidad() {
    return utilidad;
  }

  public void setTotal(Double total) {
    this.total = total;
  }

  public Double getTotal() {
    return total;
  }

  public void setIdAlmacen(Long idAlmacen) {
    this.idAlmacen = idAlmacen;
  }

  public Long getIdAlmacen() {
    return idAlmacen;
  }

  public void setTipoDeCambio(Double tipoDeCambio) {
    this.tipoDeCambio = tipoDeCambio;
  }

  public Double getTipoDeCambio() {
    return tipoDeCambio;
  }

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
  }

  public void setIdAutorizar(Long idAutorizar) {
    this.idAutorizar = idAutorizar;
  }

  public Long getIdAutorizar() {
    return idAutorizar;
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

  public void setConsecutivo(Long consecutivo) {
    this.consecutivo = consecutivo;
  }

  public Long getConsecutivo() {
    return consecutivo;
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

  public void setIdUsoCfdi(Long idUsoCfdi) {
    this.idUsoCfdi = idUsoCfdi;
  }

  public Long getIdUsoCfdi() {
    return idUsoCfdi;
  }

  public void setIdSinIva(Long idSinIva) {
    this.idSinIva = idSinIva;
  }

  public Long getIdSinIva() {
    return idSinIva;
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

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }

  public Long getIdEmpresa() {
    return idEmpresa;
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

	public Long getCticket() {
		return cticket;
	}

	public void setCticket(Long cticket) {
		this.cticket = cticket;
	}

	public Long getCcotizacion() {
		return ccotizacion;
	}

	public void setCcotizacion(Long ccotizacion) {
		this.ccotizacion = ccotizacion;
	}

	public Date getVigencia() {
		return vigencia;
	}

	public void setVigencia(Date vigencia) {
		this.vigencia = vigencia;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getCotizacion() {
		return cotizacion;
	}

	public void setCotizacion(String cotizacion) {
		this.cotizacion = cotizacion;
	}	

	public Timestamp getCobro() {
		return cobro;
	}

	public void setCobro(Timestamp cobro) {
		this.cobro = cobro;
	}	

	public Long getIdClienteDomicilio() {
		return idClienteDomicilio;
	}

	public void setIdClienteDomicilio(Long idClienteDomicilio) {
		this.idClienteDomicilio=idClienteDomicilio;
	}

	public Long getIdTipoMedioPago() {
		return idTipoMedioPago;
	}

	public void setIdTipoMedioPago(Long idTipoMedioPago) {
		this.idTipoMedioPago=idTipoMedioPago;
	}

	public Long getIdTipoPago() {
		return idTipoPago;
	}

	public void setIdTipoPago(Long idTipoPago) {
		this.idTipoPago=idTipoPago;
	}

	public Long getIdBanco() {
		return idBanco;
	}

	public void setIdBanco(Long idBanco) {
		this.idBanco=idBanco;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia=referencia;
	}

	public Long getIdTipoDocumento() {
		return idTipoDocumento;
	}

	public void setIdTipoDocumento(Long idTipoDocumento) {
		this.idTipoDocumento=idTipoDocumento;
	}

	public Long getCandado() {
		return candado;
	}

	public void setCandado(Long candado) {
		this.candado = candado;
	}	

	public Long getIdSincronizado() {
		return idSincronizado;
	}

	public void setIdSincronizado(Long idSincronizado) {
		this.idSincronizado=idSincronizado;
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
		regresar.append(getIdCredito());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getExtras());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getGlobal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getUtilidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAlmacen());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTipoDeCambio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAutorizar());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCliente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEjercicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImpuestos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsoCfdi());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdSinIva());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSubTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVenta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVentaEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTicket());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCotizacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCticket());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCcotizacion());
		regresar.append(Constantes.SEPARADOR);		
		regresar.append(getVigencia());
		regresar.append(Constantes.SEPARADOR);		
		regresar.append(getIdManual());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdFacturar());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCobro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdClienteDomicilio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoMedioPago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoPago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdBanco());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getReferencia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoDocumento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCandado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdSincronizado());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("descuentos", getDescuentos());
		regresar.put("idFactura", getIdFactura());
		regresar.put("idCredito", getIdCredito());
		regresar.put("extras", getExtras());
		regresar.put("global", getGlobal());
		regresar.put("utilidad", getUtilidad());
		regresar.put("total", getTotal());
		regresar.put("idAlmacen", getIdAlmacen());
		regresar.put("tipoDeCambio", getTipoDeCambio());
		regresar.put("orden", getOrden());
		regresar.put("idAutorizar", getIdAutorizar());
		regresar.put("idCliente", getIdCliente());
		regresar.put("descuento", getDescuento());
		regresar.put("ejercicio", getEjercicio());
		regresar.put("registro", getRegistro());
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("impuestos", getImpuestos());
		regresar.put("idUsoCfdi", getIdUsoCfdi());
		regresar.put("idSinIva", getIdSinIva());
		regresar.put("subTotal", getSubTotal());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("idVenta", getIdVenta());
		regresar.put("dia", getDia());
		regresar.put("idVentaEstatus", getIdVentaEstatus());
		regresar.put("ticket", getTicket());
		regresar.put("cotizacion", getCotizacion());
		regresar.put("cticket", getCticket());
		regresar.put("ccotizacion", getCcotizacion());
		regresar.put("vigencia", getVigencia());
		regresar.put("idManual", getIdManual());
		regresar.put("idFacturar", getIdFacturar());
		regresar.put("cobro", getCobro());
		regresar.put("idClienteDomicilio", getIdClienteDomicilio());
		regresar.put("idTipoMedioPago", getIdTipoMedioPago());
		regresar.put("idTipoPago", getIdTipoPago());
		regresar.put("idBanco", getIdBanco());
		regresar.put("referencia", getReferencia());
		regresar.put("idTipoDocumento", getIdTipoDocumento());
		regresar.put("candado", getCandado());
		regresar.put("idSincronizado", getIdSincronizado());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
			getDescuentos(), getIdFactura(), getIdCredito(), getExtras(), getGlobal(), getUtilidad(), getTotal(), getIdAlmacen(), getTipoDeCambio(), getOrden(), getIdAutorizar(), getIdCliente(), getDescuento(), getEjercicio(), getRegistro(), getConsecutivo(), getIdUsuario(), getImpuestos(), getIdUsoCfdi(), getIdSinIva(), getSubTotal(), getObservaciones(), getIdEmpresa(), getIdVenta(), getDia(), getIdVentaEstatus(), getTicket(), getCotizacion(), getTicket(), getCcotizacion(), getVigencia(), getIdManual(), getIdFacturar(), getCobro(), getIdClienteDomicilio(), getIdTipoMedioPago(), getIdTipoPago(), getIdBanco(), getReferencia(), getIdTipoDocumento(), getCandado(), getIdSincronizado()
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