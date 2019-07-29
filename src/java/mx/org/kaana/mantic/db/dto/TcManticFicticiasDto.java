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
public class TcManticFicticiasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="descuentos")
  private Double descuentos;
  @Column (name="id_tipo_pago")
  private Long idTipoPago;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_venta")
  private Long idFicticia;
  @Column (name="extras")
  private String extras;
  @Column (name="global")
  private Double global;
  @Column (name="total")
  private Double total;
  @Column (name="id_venta_estatus")
  private Long idFicticiaEstatus;
  @Column (name="tipo_de_cambio")
  private Double tipoDeCambio;
  @Column (name="orden")
  private Long orden;
  @Column (name="id_tipo_medio_pago")
  private Long idTipoMedioPago;
  @Column (name="id_cliente")
  private Long idCliente;
  @Column (name="id_cliente_domicilio")
  private Long idClienteDomicilio;
  @Column (name="descuento")
  private String descuento;
  @Column (name="id_banco")
  private Long idBanco;
  @Column (name="ejercicio")
  private Long ejercicio;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="consecutivo")
  private String consecutivo;
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
  @Column (name="dia")
  private Date dia;
  @Column (name="referencia")
  private String referencia;
  @Column (name="id_factura")
  private Long idFactura;
  @Column (name="id_tipo_documento")
  private Long idTipoDocumento;
  @Column (name="id_almacen")
  private Long idAlmacen;
  @Column (name="id_facturar")
  private Long idFacturar;
  @Column (name="ticket")
  private String ticket;
  @Column (name="cticket")
  private Long cticket;
  @Column (name="utilidad")
  private Double utilidad;
	@Column (name="vigencia")
  private Date vigencia;
	@Column (name="ccotizacion")
  private Long ccotizacion;
	@Column (name="cotizacion")
  private String cotizacion;

  public TcManticFicticiasDto() {
    this(new Long(-1L));
  }

  public TcManticFicticiasDto(Long key) {
    this(null, null, new Long(-1L), "0", null, null, null, 1D, null, null, null, null, "0", null, null, null, null, null, null, null, null, null, null, new Date(Calendar.getInstance().getTimeInMillis()), null, null);
    setKey(key);
  }

  public TcManticFicticiasDto(Double descuentos, Long idTipoPago, Long idFicticia, String extras, Double global, Double total, Long idFicticiaEstatus, Double tipoDeCambio, Long orden, Long idTipoMedioPago, Long idCliente, Long idClienteDomicilio, String descuento, Long idBanco, Long ejercicio, String consecutivo, Long idUsuario, Double impuestos, Long idUsoCfdi, Long idSinIva, Double subTotal, String observaciones, Long idEmpresa, Date dia, String referencia, Long idFactura) {
	  this(descuentos, idTipoPago, idFicticia, extras, global, total, idFicticiaEstatus, tipoDeCambio, orden, idTipoMedioPago, idCliente, idClienteDomicilio, descuento, idBanco, ejercicio, consecutivo, idUsuario, impuestos, idUsoCfdi, idSinIva, subTotal, observaciones, idEmpresa, dia, referencia, idFactura, 1L);
	}
	
  public TcManticFicticiasDto(Double descuentos, Long idTipoPago, Long idFicticia, String extras, Double global, Double total, Long idFicticiaEstatus, Double tipoDeCambio, Long orden, Long idTipoMedioPago, Long idCliente, Long idClienteDomicilio, String descuento, Long idBanco, Long ejercicio, String consecutivo, Long idUsuario, Double impuestos, Long idUsoCfdi, Long idSinIva, Double subTotal, String observaciones, Long idEmpresa, Date dia, String referencia, Long idFactura, Long idTipoDocumento) {
		this(descuentos, idTipoPago, idFicticia, extras, global, total, idFicticiaEstatus, tipoDeCambio, orden, idTipoMedioPago, idCliente, idClienteDomicilio, descuento, idBanco, ejercicio, consecutivo, idUsuario, impuestos, idUsoCfdi, idSinIva, subTotal, observaciones, idEmpresa, dia, referencia, idFactura, idTipoDocumento, null, null);
	}
	
  public TcManticFicticiasDto(Double descuentos, Long idTipoPago, Long idFicticia, String extras, Double global, Double total, Long idFicticiaEstatus, Double tipoDeCambio, Long orden, Long idTipoMedioPago, Long idCliente, Long idClienteDomicilio, String descuento, Long idBanco, Long ejercicio, String consecutivo, Long idUsuario, Double impuestos, Long idUsoCfdi, Long idSinIva, Double subTotal, String observaciones, Long idEmpresa, Date dia, String referencia, Long idFactura, Long idTipoDocumento, Long ccotizacion, String cotizacion) {
    setDescuentos(descuentos);
    setIdTipoPago(idTipoPago);
    setIdFicticia(idFicticia);
    setExtras(extras);
    setGlobal(global);
    setTotal(total);
    setIdFicticiaEstatus(idFicticiaEstatus);
    setTipoDeCambio(tipoDeCambio);
    setOrden(orden);
    setIdTipoMedioPago(idTipoMedioPago);
    setIdCliente(idCliente);
    setDescuento(descuento);
    setIdBanco(idBanco);
    setEjercicio(ejercicio);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setVigencia(new Date(Calendar.getInstance().getTimeInMillis()));
    setConsecutivo(consecutivo);
    setIdUsuario(idUsuario);
    setImpuestos(impuestos);
    setIdUsoCfdi(idUsoCfdi);
    setIdSinIva(idSinIva);
    setSubTotal(subTotal);
    setObservaciones(observaciones);
    setIdEmpresa(idEmpresa);
    setDia(dia);
    setReferencia(referencia);
		this.idClienteDomicilio= idClienteDomicilio;
		this.idFactura  = idFactura;
		this.idTipoDocumento= idTipoDocumento;
		this.idAlmacen  = 1L;
		this.idFacturar = 1L;
		this.ticket     = consecutivo;
		this.cticket    = orden;
		this.utilidad   = 0D;
		setCotizacion(cotizacion);
		setCcotizacion(ccotizacion);
  }
	
  public void setDescuentos(Double descuentos) {
    this.descuentos = descuentos;
  }

  public Double getDescuentos() {
    return descuentos;
  }

  public void setIdTipoPago(Long idTipoPago) {
    this.idTipoPago = idTipoPago;
  }

  public Long getIdTipoPago() {
    return idTipoPago;
  }

  public void setIdFicticia(Long idFicticia) {
    this.idFicticia = idFicticia;
  }

  public Long getIdFicticia() {
    return idFicticia;
  }

  public void setIdVenta(Long idFicticia) {
    this.idFicticia = idFicticia;
  }

  public Long getIdVenta() {
    return idFicticia;
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

  public void setTotal(Double total) {
    this.total = total;
  }

  public Double getTotal() {
    return total;
  }

  public void setIdFicticiaEstatus(Long idFicticiaEstatus) {
    this.idFicticiaEstatus = idFicticiaEstatus;
  }

  public Long getIdFicticiaEstatus() {
    return idFicticiaEstatus;
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
  
	public void setIdClienteDomicilio(Long idClienteDomicilio) {
    this.idClienteDomicilio = idClienteDomicilio;
  }

  public Long getIdClienteDomicilio() {
    return idClienteDomicilio;
  }

  public void setDescuento(String descuento) {
    this.descuento = descuento;
  }

  public String getDescuento() {
    return descuento;
  }

  public void setIdBanco(Long idBanco) {
    this.idBanco = idBanco;
  }

  public Long getIdBanco() {
    return idBanco;
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

  public void setDia(Date dia) {
    this.dia = dia;
  }

  public Date getDia() {
    return dia;
  }

  public void setReferencia(String referencia) {
    this.referencia = referencia;
  }

  public String getReferencia() {
    return referencia;
  }

	public Long getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(Long idFactura) {
		this.idFactura=idFactura;
	}

	public Long getIdTipoDocumento() {
		return idTipoDocumento;
	}

	public void setIdTipoDocumento(Long idTipoDocumento) {
		this.idTipoDocumento=idTipoDocumento;
	}

	public Long getIdAlmacen() {
		return idAlmacen;
	}

	public void setIdAlmacen(Long idAlmacen) {
		this.idAlmacen=idAlmacen;
	}

	public Long getIdFacturar() {
		return idFacturar;
	}

	public void setIdFacturar(Long idFacturar) {
		this.idFacturar=idFacturar;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket=ticket;
	}

	public Long getCticket() {
		return cticket;
	}

	public void setCticket(Long cticket) {
		this.cticket=cticket;
	}

	public Double getUtilidad() {
		return utilidad;
	}

	public void setUtilidad(Double utilidad) {
		this.utilidad=utilidad;
	}

		public Date getVigencia() {
		return vigencia;
	}

	public void setVigencia(Date vigencia) {
		this.vigencia = vigencia;
	}
	
	public Long getCcotizacion() {
		return ccotizacion;
	}

	public void setCcotizacion(Long ccotizacion) {
		this.ccotizacion = ccotizacion;
	}
	
	public String getCotizacion() {
		return cotizacion;
	}

	public void setCotizacion(String cotizacion) {
		this.cotizacion = cotizacion;
	}	
	
  @Transient
  @Override
  public Long getKey() {
  	return getIdFicticia();
  }

  @Override
  public void setKey(Long key) {
  	this.idFicticia = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDescuentos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoPago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdFicticia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getExtras());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getGlobal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdFicticiaEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTipoDeCambio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoMedioPago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCliente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdClienteDomicilio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdBanco());
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
		regresar.append(getDia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getReferencia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdFactura());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoDocumento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAlmacen());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdFacturar());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTicket());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCticket());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getUtilidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getVigencia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCotizacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCcotizacion());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("descuentos", getDescuentos());
		regresar.put("idTipoPago", getIdTipoPago());
		regresar.put("idFicticia", getIdFicticia());
		regresar.put("extras", getExtras());
		regresar.put("global", getGlobal());
		regresar.put("total", getTotal());
		regresar.put("idFicticiaEstatus", getIdFicticiaEstatus());
		regresar.put("tipoDeCambio", getTipoDeCambio());
		regresar.put("orden", getOrden());
		regresar.put("idTipoMedioPago", getIdTipoMedioPago());
		regresar.put("idCliente", getIdCliente());
		regresar.put("idClienteDomicilio", getIdClienteDomicilio());
		regresar.put("descuento", getDescuento());
		regresar.put("idBanco", getIdBanco());
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
		regresar.put("dia", getDia());
		regresar.put("referencia", getReferencia());
		regresar.put("idFactura", getIdFactura());
		regresar.put("idTipoDocumento", getIdTipoDocumento());
		regresar.put("idAlmacen", getIdAlmacen());
		regresar.put("idFacturar", getIdFacturar());
		regresar.put("ticket", getTicket());
		regresar.put("cticket", getCticket());
		regresar.put("utilidad", getUtilidad());
		regresar.put("idVenta", getIdVenta());
		regresar.put("vigencia", getVigencia());
		regresar.put("cotizacion", getCotizacion());
		regresar.put("ccotizacion", getCcotizacion());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
      getDescuentos(), getIdTipoPago(), getIdFicticia(), getExtras(), getGlobal(), getTotal(), getIdFicticiaEstatus(), getTipoDeCambio(), getOrden(), getIdTipoMedioPago(), getIdCliente(), getIdClienteDomicilio(), getDescuento(), getIdBanco(), getEjercicio(), getRegistro(), getConsecutivo(), getIdUsuario(), getImpuestos(), getIdUsoCfdi(), getIdSinIva(), getSubTotal(), getObservaciones(), getIdEmpresa(), getDia(), getReferencia(), getIdFactura(), getIdTipoDocumento(), getIdAlmacen(), getIdFacturar(), getTicket(), getCticket(), getUtilidad(), getVigencia(), getCotizacion(), getCcotizacion()
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
    regresar.append("idFicticia~");
    regresar.append(getIdFicticia());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdFicticia());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticFicticiasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdFicticia()!= null && getIdFicticia()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticFicticiasDto other = (TcManticFicticiasDto) obj;
    if (getIdFicticia() != other.idFicticia && (getIdFicticia() == null || !getIdFicticia().equals(other.idFicticia))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdFicticia() != null ? getIdFicticia().hashCode() : 0);
    return hash;
  }
}