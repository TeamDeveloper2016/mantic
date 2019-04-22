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
@Table(name="tc_mantic_clientes_pagos")
public class TcManticClientesPagosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_cliente_deuda")
  private Long idClienteDeuda;
  @Column (name="observaciones")
  private String observaciones;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_cliente_pago")
  private Long idClientePago;
  @Column (name="pago")
  private Double pago;
  @Column (name="registro")
  private Timestamp registro;
	@Column (name="id_tipo_medio_pago")
  private Long idTipoMedioPago;
	@Column (name="id_cierre")
  private Long idCierre;
	@Column (name="id_banco")
  private Long idBanco;
	@Column (name="referencia")
  private String referencia;
	@Column (name="consecutivo")
  private String consecutivo;
	@Column (name="orden")
  private Long orden;
	@Column (name="ejercicio")
  private Long ejercicio;

  public TcManticClientesPagosDto() {
    this(new Long(-1L));
  }

  public TcManticClientesPagosDto(Long key) {
    this(null, null, null, new Long(-1L), null, null, null, null, null);
    setKey(key);
  }

  public TcManticClientesPagosDto(Long idUsuario, Long idClienteDeuda, String observaciones, Long idClientePago, Double pago, Long idTipoMedioPago, Long idCierre, Long idBanco, String referencia) {
		this(idUsuario, idClienteDeuda, observaciones, idClientePago, pago, idTipoMedioPago, idCierre, idBanco, referencia, null, null, null);
	}
	
  public TcManticClientesPagosDto(Long idUsuario, Long idClienteDeuda, String observaciones, Long idClientePago, Double pago, Long idTipoMedioPago, Long idCierre, Long idBanco, String referencia, String consecutivo, Long orden, Long ejercicio) {
    setIdUsuario(idUsuario);
    setIdClienteDeuda(idClienteDeuda);
    setObservaciones(observaciones);
    setIdClientePago(idClientePago);
    setPago(pago);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		setIdTipoMedioPago(idTipoMedioPago);
		setIdCierre(idCierre); 
		setIdBanco(idBanco);
		setReferencia(referencia);
		setConsecutivo(consecutivo);
		setOrden(orden);
		setEjercicio(ejercicio);
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
	
	public Long getIdCierre() {
		return idCierre;
	}

	public void setIdCierre(Long idCierre) {
		this.idCierre = idCierre;
	}
	
  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdClienteDeuda(Long idClienteDeuda) {
    this.idClienteDeuda = idClienteDeuda;
  }

  public Long getIdClienteDeuda() {
    return idClienteDeuda;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setIdClientePago(Long idClientePago) {
    this.idClientePago = idClientePago;
  }

  public Long getIdClientePago() {
    return idClientePago;
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

	public Long getIdTipoMedioPago() {
		return idTipoMedioPago;
	}

	public void setIdTipoMedioPago(Long idTipoMedioPago) {
		this.idTipoMedioPago = idTipoMedioPago;
	}

	public String getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public Long getOrden() {
		return orden;
	}

	public void setOrden(Long orden) {
		this.orden = orden;
	}

	public Long getEjercicio() {
		return ejercicio;
	}

	public void setEjercicio(Long ejercicio) {
		this.ejercicio = ejercicio;
	}
	
  @Transient
  @Override
  public Long getKey() {
  	return getIdClientePago();
  }

  @Override
  public void setKey(Long key) {
  	this.idClientePago = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdClienteDeuda());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdClientePago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoMedioPago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCierre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdBanco());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getReferencia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEjercicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idClienteDeuda", getIdClienteDeuda());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idClientePago", getIdClientePago());
		regresar.put("pago", getPago());
		regresar.put("registro", getRegistro());
		regresar.put("idTipoMedioPago", getIdTipoMedioPago());
		regresar.put("idCierre", getIdCierre());
		regresar.put("idBanco", getIdBanco());
		regresar.put("referencia", getReferencia());
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("ejercicio", getEjercicio());
		regresar.put("orden", getOrden());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
			getIdUsuario(), getIdClienteDeuda(), getObservaciones(), getIdClientePago(), getPago(), getRegistro(), getIdTipoMedioPago(), getIdCierre(), getIdBanco(), getReferencia(), getConsecutivo(), getOrden(), getEjercicio()
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
    regresar.append("idClientePago~");
    regresar.append(getIdClientePago());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdClientePago());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticClientesPagosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdClientePago()!= null && getIdClientePago()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticClientesPagosDto other = (TcManticClientesPagosDto) obj;
    if (getIdClientePago() != other.idClientePago && (getIdClientePago() == null || !getIdClientePago().equals(other.idClientePago))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdClientePago() != null ? getIdClientePago().hashCode() : 0);
    return hash;
  }
}