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
@Table(name="tc_mantic_servicios")
public class TcManticServiciosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="descuentos")
  private Double descuentos;
  @Column (name="id_factura")
  private Long idFactura;
  @Column (name="caracteristicas")
  private String caracteristicas;
  @Column (name="fecha_estimada")
  private Date fechaEstimada;
  @Column (name="id_tipo_medio_pago")
  private Long idTipoMedioPago;
  @Column (name="id_cliente")
  private Long idCliente;
  @Column (name="herramienta")
  private String herramienta;
  @Column (name="id_servicio_estatus")
  private Long idServicioEstatus;
  @Column (name="descuento")
  private String descuento;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_servicio")
  private Long idServicio;
	@Column (name="id_garantia")
  private Long idGarantia;
  @Column (name="modelo")
  private String modelo;
  @Column (name="ejercicio")
  private Long ejercicio;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="consecutivo")
  private String consecutivo;
  @Column (name="marca")
  private String marca;
  @Column (name="total")
  private Double total;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="impuestos")
  private Double impuestos;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="sub_total")
  private Double subTotal;
  @Column (name="efectivo")
  private Double efectivo;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="reparacion")
  private String reparacion;
  @Column (name="orden")
  private Long orden;
  @Column (name="id_particular")
  private Long idParticular;
  @Column (name="cliente")
  private String cliente;
  @Column (name="telefonos")
  private String telefonos;
	@Column (name="id_venta")
  private Long idVenta;

  public TcManticServiciosDto() {
    this(new Long(-1L));
  }

  public TcManticServiciosDto(Long key) {
    this(0D, null, null, new Date(Calendar.getInstance().getTimeInMillis()), null, null, null, null, "0", new Long(-1L), null, null, null, null, null, null, null, null, null, null, null, null, null, null, 1L, null, null, null);
    setKey(key);
  }

  public TcManticServiciosDto(Double descuentos, Long idFactura, String caracteristicas, Date fechaEstimada, Long idTipoMedioPago, Long idCliente, String herramienta, Long idServicioEstatus, String descuento, Long idServicio, String modelo, Long ejercicio, String consecutivo, String marca, Double total, Long idUsuario, Double impuestos, String observaciones, Double subTotal, Double efectivo, Long idEmpresa, String reparacion, Long orden, Long idGarantia, Long idParticular, String cliente, String telefonos, Long idVenta) {
    setDescuentos(descuentos);
    setIdFactura(idFactura);
    setCaracteristicas(caracteristicas);
    setFechaEstimada(fechaEstimada);
    setIdTipoMedioPago(idTipoMedioPago);
    setIdCliente(idCliente);
    setHerramienta(herramienta);
    setIdServicioEstatus(idServicioEstatus);
    setDescuento(descuento);
    setIdServicio(idServicio);
    setModelo(modelo);
    setEjercicio(ejercicio);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setConsecutivo(consecutivo);
    setMarca(marca);
    setTotal(total);
    setIdUsuario(idUsuario);
    setImpuestos(impuestos);
    setObservaciones(observaciones);
    setSubTotal(subTotal);
    setEfectivo(efectivo);
    setIdEmpresa(idEmpresa);
    setReparacion(reparacion);
    setOrden(orden);
		setIdGarantia(idGarantia);
		this.idParticular= idParticular;
		this.cliente= cliente;
		this.telefonos= telefonos;
		setIdVenta(idVenta);
  }

	public Long getIdGarantia() {
		return idGarantia;
	}

	public void setIdGarantia(Long idGarantia) {
		this.idGarantia = idGarantia;
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

  public void setCaracteristicas(String caracteristicas) {
    this.caracteristicas = caracteristicas;
  }

  public String getCaracteristicas() {
    return caracteristicas;
  }

  public void setFechaEstimada(Date fechaEstimada) {
    this.fechaEstimada = fechaEstimada;
  }

  public Date getFechaEstimada() {
    return fechaEstimada;
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

  public void setHerramienta(String herramienta) {
    this.herramienta = herramienta;
  }

  public String getHerramienta() {
    return herramienta;
  }

  public void setIdServicioEstatus(Long idServicioEstatus) {
    this.idServicioEstatus = idServicioEstatus;
  }

  public Long getIdServicioEstatus() {
    return idServicioEstatus;
  }

  public void setDescuento(String descuento) {
    this.descuento = descuento;
  }

  public String getDescuento() {
    return descuento;
  }

  public void setIdServicio(Long idServicio) {
    this.idServicio = idServicio;
  }

  public Long getIdServicio() {
    return idServicio;
  }

  public void setModelo(String modelo) {
    this.modelo = modelo;
  }

  public String getModelo() {
    return modelo;
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

  public void setMarca(String marca) {
    this.marca = marca;
  }

  public String getMarca() {
    return marca;
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

  public void setImpuestos(Double impuestos) {
    this.impuestos = impuestos;
  }

  public Double getImpuestos() {
    return impuestos;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setSubTotal(Double subTotal) {
    this.subTotal = subTotal;
  }

  public Double getSubTotal() {
    return subTotal;
  }

  public void setEfectivo(Double efectivo) {
    this.efectivo = efectivo;
  }

  public Double getEfectivo() {
    return efectivo;
  }

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }

  public Long getIdEmpresa() {
    return idEmpresa;
  }

  public void setReparacion(String reparacion) {
    this.reparacion = reparacion;
  }

  public String getReparacion() {
    return reparacion;
  }

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
  }

	public Long getIdParticular() {
		return idParticular;
	}

	public void setIdParticular(Long idParticular) {
		this.idParticular=idParticular;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente=cliente;
	}

	public String getTelefonos() {
		return telefonos;
	}

	public void setTelefonos(String telefonos) {
		this.telefonos=telefonos;
	}

	public Long getIdVenta() {
		return idVenta;
	}

	public void setIdVenta(Long idVenta) {
		this.idVenta = idVenta;
	}	
	
  @Transient
  @Override
  public Long getKey() {
  	return getIdServicio();
  }

  @Override
  public void setKey(Long key) {
  	this.idServicio = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDescuentos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdFactura());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCaracteristicas());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFechaEstimada());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoMedioPago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCliente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getHerramienta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdServicioEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdServicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getModelo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEjercicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMarca());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImpuestos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSubTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEfectivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getReparacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdGarantia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdParticular());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCliente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTelefonos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVenta());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("descuentos", getDescuentos());
		regresar.put("idFactura", getIdFactura());
		regresar.put("caracteristicas", getCaracteristicas());
		regresar.put("fechaEstimada", getFechaEstimada());
		regresar.put("idTipoMedioPago", getIdTipoMedioPago());
		regresar.put("idCliente", getIdCliente());
		regresar.put("herramienta", getHerramienta());
		regresar.put("idServicioEstatus", getIdServicioEstatus());
		regresar.put("descuento", getDescuento());
		regresar.put("idServicio", getIdServicio());
		regresar.put("modelo", getModelo());
		regresar.put("ejercicio", getEjercicio());
		regresar.put("registro", getRegistro());
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("marca", getMarca());
		regresar.put("total", getTotal());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("impuestos", getImpuestos());
		regresar.put("observaciones", getObservaciones());
		regresar.put("subTotal", getSubTotal());
		regresar.put("efectivo", getEfectivo());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("reparacion", getReparacion());
		regresar.put("idGarantia", getIdGarantia());
		regresar.put("orden", getOrden());
		regresar.put("idParticular", getIdParticular());
		regresar.put("cliente", getCliente());
		regresar.put("telefonos", getTelefonos());
		regresar.put("idVenta", getIdVenta());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
      getDescuentos(), getIdFactura(), getCaracteristicas(), getFechaEstimada(), getIdTipoMedioPago(), getIdCliente(), getHerramienta(), getIdServicioEstatus(), getDescuento(), getIdServicio(), getModelo(), getEjercicio(), getRegistro(), getConsecutivo(), getMarca(), getTotal(), getIdUsuario(), getImpuestos(), getObservaciones(), getSubTotal(), getEfectivo(), getIdEmpresa(), getReparacion(), getIdGarantia(), getOrden(), getIdParticular(), getCliente(), getTelefonos(), getIdVenta()
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
    regresar.append("idServicio~");
    regresar.append(getIdServicio());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdServicio());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticServiciosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdServicio()!= null && getIdServicio()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticServiciosDto other = (TcManticServiciosDto) obj;
    if (getIdServicio() != other.idServicio && (getIdServicio() == null || !getIdServicio().equals(other.idServicio))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdServicio() != null ? getIdServicio().hashCode() : 0);
    return hash;
  }
}