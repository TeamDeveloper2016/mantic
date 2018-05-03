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
@Table(name="tc_mantic_notas_entradas")
public class TcManticNotasEntradasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="consecutivo")
  private String consecutivo;
  @Column (name="fecha")
  private Date fecha;
  @Column (name="id_proveedor")
  private Long idProveedor;
  @Column (name="total")
  private Double total;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="subtotal")
  private Double subtotal;
  @Column (name="descuento")
  private Double descuento;
  @Column (name="observaciones")
  private String observaciones;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_nota_entrada")
  private Long idNotaEntrada;
  @Column (name="id_estatus")
  private Long idEstatus;
  @Column (name="total_impuestos")
  private Double totalImpuestos;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticNotasEntradasDto() {
    this(new Long(-1L));
  }

  public TcManticNotasEntradasDto(Long key) {
    this(null, new Date(Calendar.getInstance().getTimeInMillis()), null, null, null, null, null, null, new Long(-1L), null, null);
    setKey(key);
  }

  public TcManticNotasEntradasDto(String consecutivo, Date fecha, Long idProveedor, Double total, Long idUsuario, Double subtotal, Double descuento, String observaciones, Long idNotaEntrada, Long idEstatus, Double totalImpuestos) {
    setConsecutivo(consecutivo);
    setFecha(fecha);
    setIdProveedor(idProveedor);
    setTotal(total);
    setIdUsuario(idUsuario);
    setSubtotal(subtotal);
    setDescuento(descuento);
    setObservaciones(observaciones);
    setIdNotaEntrada(idNotaEntrada);
    setIdEstatus(idEstatus);
    setTotalImpuestos(totalImpuestos);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setConsecutivo(String consecutivo) {
    this.consecutivo = consecutivo;
  }

  public String getConsecutivo() {
    return consecutivo;
  }

  public void setFecha(Date fecha) {
    this.fecha = fecha;
  }

  public Date getFecha() {
    return fecha;
  }

  public void setIdProveedor(Long idProveedor) {
    this.idProveedor = idProveedor;
  }

  public Long getIdProveedor() {
    return idProveedor;
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

  public void setSubtotal(Double subtotal) {
    this.subtotal = subtotal;
  }

  public Double getSubtotal() {
    return subtotal;
  }

  public void setDescuento(Double descuento) {
    this.descuento = descuento;
  }

  public Double getDescuento() {
    return descuento;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setIdNotaEntrada(Long idNotaEntrada) {
    this.idNotaEntrada = idNotaEntrada;
  }

  public Long getIdNotaEntrada() {
    return idNotaEntrada;
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

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdNotaEntrada();
  }

  @Override
  public void setKey(Long key) {
  	this.idNotaEntrada = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFecha());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdProveedor());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSubtotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdNotaEntrada());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTotalImpuestos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("fecha", getFecha());
		regresar.put("idProveedor", getIdProveedor());
		regresar.put("total", getTotal());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("subtotal", getSubtotal());
		regresar.put("descuento", getDescuento());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idNotaEntrada", getIdNotaEntrada());
		regresar.put("idEstatus", getIdEstatus());
		regresar.put("totalImpuestos", getTotalImpuestos());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getConsecutivo(), getFecha(), getIdProveedor(), getTotal(), getIdUsuario(), getSubtotal(), getDescuento(), getObservaciones(), getIdNotaEntrada(), getIdEstatus(), getTotalImpuestos(), getRegistro()
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
    regresar.append("idNotaEntrada~");
    regresar.append(getIdNotaEntrada());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdNotaEntrada());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticNotasEntradasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdNotaEntrada()!= null && getIdNotaEntrada()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticNotasEntradasDto other = (TcManticNotasEntradasDto) obj;
    if (getIdNotaEntrada() != other.idNotaEntrada && (getIdNotaEntrada() == null || !getIdNotaEntrada().equals(other.idNotaEntrada))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdNotaEntrada() != null ? getIdNotaEntrada().hashCode() : 0);
    return hash;
  }

}


