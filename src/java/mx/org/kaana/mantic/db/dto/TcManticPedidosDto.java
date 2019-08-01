package mx.org.kaana.mantic.db.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
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
@Table(name="tc_mantic_pedidos")
public class TcManticPedidosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="descuentos")
  private Double descuentos;
  @Column (name="descuento")
  private String descuento;
  @Column (name="extras")
  private String extras;
  @Column (name="ejercicio")
  private Long ejercicio;
  @Column (name="id_pedido_estatus")
  private Long idPedidoEstatus;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="consecutivo")
  private String consecutivo;
  @Column (name="total")
  private Double total;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="excedentes")
  private Double excedentes;
  @Column (name="impuestos")
  private Double impuestos;
  @Column (name="sub_total")
  private Double subTotal;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="orden")
  private Long orden;
	@Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  @Column (name="id_pedido")
  private Long idPedido;

  public TcManticPedidosDto() {
    this(new Long(-1L));
  }

  public TcManticPedidosDto(Long key) {
    this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticPedidosDto(Double descuentos, String descuento, String extras, Long ejercicio, Long idPedidoEstatus, String consecutivo, Double total, Long idUsuario, Double excedentes, Double impuestos, Double subTotal, String observaciones, Long idEmpresa, Long orden, Long idPedido) {
    setDescuentos(descuentos);
    setDescuento(descuento);
    setExtras(extras);
    setEjercicio(ejercicio);
    setIdPedidoEstatus(idPedidoEstatus);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setConsecutivo(consecutivo);
    setTotal(total);
    setIdUsuario(idUsuario);
    setExcedentes(excedentes);
    setImpuestos(impuestos);
    setSubTotal(subTotal);
    setObservaciones(observaciones);
    setIdEmpresa(idEmpresa);
    setOrden(orden);
    setIdPedido(idPedido);
  }
	
  public void setDescuentos(Double descuentos) {
    this.descuentos = descuentos;
  }

  public Double getDescuentos() {
    return descuentos;
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

  public void setEjercicio(Long ejercicio) {
    this.ejercicio = ejercicio;
  }

  public Long getEjercicio() {
    return ejercicio;
  }

  public void setIdPedidoEstatus(Long idPedidoEstatus) {
    this.idPedidoEstatus = idPedidoEstatus;
  }

  public Long getIdPedidoEstatus() {
    return idPedidoEstatus;
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

  public void setExcedentes(Double excedentes) {
    this.excedentes = excedentes;
  }

  public Double getExcedentes() {
    return excedentes;
  }

  public void setImpuestos(Double impuestos) {
    this.impuestos = impuestos;
  }

  public Double getImpuestos() {
    return impuestos;
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

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
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
  	return getIdPedido();
  }

  @Override
  public void setKey(Long key) {
  	this.idPedido = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDescuentos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getExtras());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEjercicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPedidoEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getExcedentes());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImpuestos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSubTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPedido());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("descuentos", getDescuentos());
		regresar.put("descuento", getDescuento());
		regresar.put("extras", getExtras());
		regresar.put("ejercicio", getEjercicio());
		regresar.put("idPedidoEstatus", getIdPedidoEstatus());
		regresar.put("registro", getRegistro());
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("total", getTotal());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("excedentes", getExcedentes());
		regresar.put("impuestos", getImpuestos());
		regresar.put("subTotal", getSubTotal());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("orden", getOrden());
		regresar.put("idPedido", getIdPedido());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getDescuentos(), getDescuento(), getExtras(), getEjercicio(), getIdPedidoEstatus(), getRegistro(), getConsecutivo(), getTotal(), getIdUsuario(), getExcedentes(), getImpuestos(), getSubTotal(), getObservaciones(), getIdEmpresa(), getOrden(), getIdPedido()
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
    regresar.append("idPedido~");
    regresar.append(getIdPedido());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdPedido());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticPedidosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdPedido()!= null && getIdPedido()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticPedidosDto other = (TcManticPedidosDto) obj;
    if (getIdPedido() != other.idPedido && (getIdPedido() == null || !getIdPedido().equals(other.idPedido))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdPedido() != null ? getIdPedido().hashCode() : 0);
    return hash;
  }

}


