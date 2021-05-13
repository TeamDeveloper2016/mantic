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
@Table(name="tc_mantic_empresas_pagos_controles")
public class TcManticEmpresasPagosControlesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="tipo")
  private String tipo;
  @Column (name="id_activo")
  private Long idActivo;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="observaciones")
  private String observaciones;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_empresa_pago_control")
  private Long idEmpresaPagoControl;
  @Column (name="pago")
  private Double pago;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticEmpresasPagosControlesDto() {
    this(new Long(-1L));
  }

  public TcManticEmpresasPagosControlesDto(Long key) {
    this(null, null, null, null, new Long(-1L), null);
    setKey(key);
  }

  public TcManticEmpresasPagosControlesDto(String tipo, Long idActivo, Long idUsuario, String observaciones, Long idEmpresaPagoControl, Double pago) {
    setTipo(tipo);
    setIdActivo(idActivo);
    setIdUsuario(idUsuario);
    setObservaciones(observaciones);
    setIdEmpresaPagoControl(idEmpresaPagoControl);
    setPago(pago);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getTipo() {
    return tipo;
  }

  public void setIdActivo(Long idActivo) {
    this.idActivo = idActivo;
  }

  public Long getIdActivo() {
    return idActivo;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setIdEmpresaPagoControl(Long idEmpresaPagoControl) {
    this.idEmpresaPagoControl = idEmpresaPagoControl;
  }

  public Long getIdEmpresaPagoControl() {
    return idEmpresaPagoControl;
  }

  public void setPago(Double pago) {
    this.pago = pago;
  }

  public Double getPago() {
    return pago;
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
  	return getIdEmpresaPagoControl();
  }

  @Override
  public void setKey(Long key) {
  	this.idEmpresaPagoControl = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getTipo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdActivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresaPagoControl());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("tipo", getTipo());
		regresar.put("idActivo", getIdActivo());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idEmpresaPagoControl", getIdEmpresaPagoControl());
		regresar.put("pago", getPago());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getTipo(), getIdActivo(), getIdUsuario(), getObservaciones(), getIdEmpresaPagoControl(), getPago(), getRegistro()
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
    regresar.append("idEmpresaPagoControl~");
    regresar.append(getIdEmpresaPagoControl());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdEmpresaPagoControl());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticEmpresasPagosControlesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdEmpresaPagoControl()!= null && getIdEmpresaPagoControl()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticEmpresasPagosControlesDto other = (TcManticEmpresasPagosControlesDto) obj;
    if (getIdEmpresaPagoControl() != other.idEmpresaPagoControl && (getIdEmpresaPagoControl() == null || !getIdEmpresaPagoControl().equals(other.idEmpresaPagoControl))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdEmpresaPagoControl() != null ? getIdEmpresaPagoControl().hashCode() : 0);
    return hash;
  }

}


