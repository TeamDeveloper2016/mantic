package mx.org.kaana.mantic.db.dto;

import java.io.Serializable;
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
@Table(name="tc_mantic_apartados_pagos")
public class TcManticApartadosPagosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_apartado_pago")
  private Long idApartadoPago;
  @Column (name="id_tipo_medio_pago")
  private Long idTipoMedioPago;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="id_apartado")
  private Long idApartado;
  @Column (name="pago")
  private Double pago;
  @Column (name="registro")
  private Timestamp registro;
	@Column (name="id_cierre")
  private Long idCierre;
  @Column (name="id_banco")
  private Long idBanco;
	@Column (name="referencia")
  private String referencia;

  public TcManticApartadosPagosDto() {
    this(new Long(-1L));
  }

  public TcManticApartadosPagosDto(Long key) {
    this(new Long(-1L), null, null, null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticApartadosPagosDto(Long idApartadoPago, Long idTipoMedioPago, Long idUsuario, String observaciones, Long idApartado, Double pago, Long idCierre, Long idBanco, String referencia) {
    setIdApartadoPago(idApartadoPago);
    setIdTipoMedioPago(idTipoMedioPago);
    setIdUsuario(idUsuario);
    setObservaciones(observaciones);
    setIdApartado(idApartado);
    setPago(pago);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		setIdCierre(idCierre);
    setIdBanco(idBanco);
		setReferencia(referencia);
  }

	public Long getIdCierre() {
		return idCierre;
	}

	public void setIdCierre(Long idCierre) {
		this.idCierre = idCierre;
	}
	
  public void setIdApartadoPago(Long idApartadoPago) {
    this.idApartadoPago = idApartadoPago;
  }

  public Long getIdApartadoPago() {
    return idApartadoPago;
  }

  public void setIdTipoMedioPago(Long idTipoMedioPago) {
    this.idTipoMedioPago = idTipoMedioPago;
  }

  public Long getIdTipoMedioPago() {
    return idTipoMedioPago;
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

  public void setIdApartado(Long idApartado) {
    this.idApartado = idApartado;
  }

  public Long getIdApartado() {
    return idApartado;
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
  
  public Long getIdBanco() {
		return idBanco;
	}

	public void setIdBanco(Long idBanco) {
		this.idBanco = idBanco;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

  @Transient
  @Override
  public Long getKey() {
  	return getIdApartadoPago();
  }

  @Override
  public void setKey(Long key) {
  	this.idApartadoPago = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdApartadoPago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoMedioPago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdApartado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCierre());
    regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdBanco());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getReferencia());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idApartadoPago", getIdApartadoPago());
		regresar.put("idTipoMedioPago", getIdTipoMedioPago());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idApartado", getIdApartado());
		regresar.put("pago", getPago());
		regresar.put("registro", getRegistro());
		regresar.put("idCierre", getIdCierre());
    regresar.put("idBanco", getIdBanco());
		regresar.put("referencia", getReferencia());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
			getIdApartadoPago(), getIdTipoMedioPago(), getIdUsuario(), getObservaciones(), getIdApartado(), getPago(), getRegistro(), getIdCierre(), getIdBanco(), getReferencia()
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
    regresar.append("idApartadoPago~");
    regresar.append(getIdApartadoPago());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdApartadoPago());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticApartadosPagosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdApartadoPago()!= null && getIdApartadoPago()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticApartadosPagosDto other = (TcManticApartadosPagosDto) obj;
    if (getIdApartadoPago() != other.idApartadoPago && (getIdApartadoPago() == null || !getIdApartadoPago().equals(other.idApartadoPago))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdApartadoPago() != null ? getIdApartadoPago().hashCode() : 0);
    return hash;
  }
}