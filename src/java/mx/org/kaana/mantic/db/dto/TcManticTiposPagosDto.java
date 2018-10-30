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
@Table(name="tc_mantic_tipos_pagos")
public class TcManticTiposPagosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="clave")
  private String clave;
  @Column (name="descripcion")
  private String descripcion;
  @Column (name="id_tipo_medio_pago")
  private Long idTipoMedioPago;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_tipo_pago")
  private Long idTipoPago;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="descuento")
  private Double descuento;
  @Column (name="nombre")
  private String nombre;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticTiposPagosDto() {
    this(new Long(-1L));
  }

  public TcManticTiposPagosDto(Long key) {
    this(null, null, new Long(-1L), null, null, null, null);
    setKey(key);
  }

  public TcManticTiposPagosDto(String descripcion, Long idTipoMedioPago, Long idTipoPago, Long idUsuario, Double descuento, String nombre, String clave) {
    setDescripcion(descripcion);
    setIdTipoMedioPago(idTipoMedioPago);
    setIdTipoPago(idTipoPago);
    setIdUsuario(idUsuario);
    setDescuento(descuento);
    setNombre(nombre);
		this.clave= clave;
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave=clave;
	}
	
  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setIdTipoMedioPago(Long idTipoMedioPago) {
    this.idTipoMedioPago = idTipoMedioPago;
  }

  public Long getIdTipoMedioPago() {
    return idTipoMedioPago;
  }

  public void setIdTipoPago(Long idTipoPago) {
    this.idTipoPago = idTipoPago;
  }

  public Long getIdTipoPago() {
    return idTipoPago;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setDescuento(Double descuento) {
    this.descuento = descuento;
  }

  public Double getDescuento() {
    return descuento;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
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
  	return getIdTipoPago();
  }

  @Override
  public void setKey(Long key) {
  	this.idTipoPago = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getClave());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoMedioPago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoPago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("clave", getClave());
		regresar.put("descripcion", getDescripcion());
		regresar.put("idTipoMedioPago", getIdTipoMedioPago());
		regresar.put("idTipoPago", getIdTipoPago());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("descuento", getDescuento());
		regresar.put("nombre", getNombre());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getClave(), getDescripcion(), getIdTipoMedioPago(), getIdTipoPago(), getIdUsuario(), getDescuento(), getNombre(), getRegistro()
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
    regresar.append("idTipoPago~");
    regresar.append(getIdTipoPago());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdTipoPago());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticTiposPagosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdTipoPago()!= null && getIdTipoPago()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticTiposPagosDto other = (TcManticTiposPagosDto) obj;
    if (getIdTipoPago() != other.idTipoPago && (getIdTipoPago() == null || !getIdTipoPago().equals(other.idTipoPago))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdTipoPago() != null ? getIdTipoPago().hashCode() : 0);
    return hash;
  }

}


