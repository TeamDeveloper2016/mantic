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
		
  private static final long serialVersionUID=1L;
  @Column (name="descuentos")
  private Double descuentos;
  @Column (name="id_factura")
  private Long idFactura;
  @Column (name="id_credito")
  private Long idCredito;
  @Column (name="extras")
  private String extras;
  @Column (name="total")
  private Double total;  
  @Column (name="id_almacen")
  private Long idAlmacen;
  @Column (name="tipo_de_cambio")
  private Double tipoDeCambio;
  @Column (name="orden")
  private Long orden;  
  @Column (name="id_cliente")
  private Long idCliente;
  @Column (name="descuento")
  private String descuento;
  @Column (name="ejercicio")
  private Long ejercicio;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="consecutivo")
  private Long consecutivo;
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
  @Column (name="id_autorizar")
  private Long idAutorizar;

  public TcManticVentasDto() {
    this(new Long(-1L));
  }

  public TcManticVentasDto(Long key) {
    this(null, null, null, null, null, null, null,  null, null, null, null, null, null, null, null, null, null, null, null, new Long(-1L), new Date(Calendar.getInstance().getTimeInMillis()), null, null);
    setKey(key);
  }
  
  public TcManticVentasDto(Double descuentos, Long idFactura, Long idCredito, String extras, Double total, Long idAlmacen, Double tipoDeCambio, Long orden, Long idCliente, String descuento, Long ejercicio, Long consecutivo, Long idUsuario, Double impuestos, Long idUsoCfdi, Long idSinIva, Double subTotal, String observaciones, Long idEmpresa, Long idVenta, Date dia, Long idVentaEstatus, Long idAutorizar) {
    setDescuentos(descuentos);
    setIdFactura(idFactura);
    setIdCredito(idCredito);
    setExtras(extras);
    setTotal(total);    
    setIdAlmacen(idAlmacen);
    setTipoDeCambio(tipoDeCambio);
    setOrden(orden);
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
		setIdAutorizar(idAutorizar);
  }

	public Long getIdAutorizar() {
		return idAutorizar;
	}

	public void setIdAutorizar(Long idAutorizar) {
		this.idAutorizar = idAutorizar;
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
		regresar.append(getTotal());
		regresar.append(Constantes.SEPARADOR);		
		regresar.append(getIdAlmacen());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTipoDeCambio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());		
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
		regresar.append(getIdAutorizar());
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
		regresar.put("total", getTotal());
		regresar.put("idAlmacen", getIdAlmacen());
		regresar.put("tipoDeCambio", getTipoDeCambio());
		regresar.put("orden", getOrden());
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
		regresar.put("idAutorizar", getIdAutorizar());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getDescuentos(), getIdFactura(), getIdCredito(), getExtras(), getTotal(), getIdAlmacen(), getTipoDeCambio(), getOrden(), getIdCliente(), getDescuento(), getEjercicio(), getRegistro(), getConsecutivo(), getIdUsuario(), getImpuestos(), getIdUsoCfdi(), getIdSinIva(), getSubTotal(), getObservaciones(), getIdEmpresa(), getIdVenta(), getDia(), getIdVentaEstatus(), getIdAutorizar()
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