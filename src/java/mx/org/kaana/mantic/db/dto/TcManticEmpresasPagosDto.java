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
@Table(name="tc_mantic_empresas_pagos")
public class TcManticEmpresasPagosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_tipo_medio_pago")
  private Long idTipoMedioPago;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_empresa_deuda")
  private Long idEmpresaDeuda;
  @Column (name="observaciones")
  private String observaciones;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_empresa_pago")
  private Long idEmpresaPago;
  @Column (name="pago")
  private Double pago;
  @Column (name="id_credito_nota")
  private Long idCreditoNota;
  @Column (name="id_nota_entrada")
  private Long idNotaEntrada;
  @Column (name="id_banco")
  private Long idBanco;
  @Column (name="referencia")
  private String referencia;
  @Column (name="registro")
  private Timestamp registro;
	@Column (name="id_cierre")
  private Long idCierre;
	@Column (name="id_egreso")
  private Long idEgreso;
	@Column (name="consecutivo")
  private String consecutivo;
	@Column (name="orden")
  private Long orden;
	@Column (name="ejercicio")
  private Long ejercicio;
	
  public TcManticEmpresasPagosDto() {
    this(new Long(-1L));
  }

  public TcManticEmpresasPagosDto(Long key) {
    this(null, null, null, null, new Long(-1L), null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticEmpresasPagosDto(Long idTipoMedioPago, Long idUsuario, Long idEmpresaDeuda, String observaciones, Long idEmpresaPago, Double pago, Long idCreditoNota, Long idBanco, String referencia, Long idNotaEntrada, Long idCierre) {
		this(idTipoMedioPago, idUsuario, idEmpresaDeuda, observaciones, idEmpresaPago, pago, idCreditoNota, idBanco, referencia, idNotaEntrada, idCierre, null, null, null);
	}
	
  public TcManticEmpresasPagosDto(Long idTipoMedioPago, Long idUsuario, Long idEmpresaDeuda, String observaciones, Long idEmpresaPago, Double pago, Long idCreditoNota, Long idBanco, String referencia, Long idNotaEntrada, Long idCierre, String consecutivo, Long orden, Long ejercicio) {
    setIdTipoMedioPago(idTipoMedioPago);
    setIdUsuario(idUsuario);
    setIdEmpresaDeuda(idEmpresaDeuda);
    setObservaciones(observaciones);
    setIdEmpresaPago(idEmpresaPago);
    setPago(pago);
    setIdCreditoNota(idCreditoNota);
    setIdBanco(idBanco);
    setReferencia(referencia);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		this.idNotaEntrada= idNotaEntrada;
		setIdCierre(idCierre);
		setConsecutivo(consecutivo);
		setOrden(orden);
		setEjercicio(ejercicio);
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

  public void setIdEmpresaDeuda(Long idEmpresaDeuda) {
    this.idEmpresaDeuda = idEmpresaDeuda;
  }

  public Long getIdEmpresaDeuda() {
    return idEmpresaDeuda;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setIdEmpresaPago(Long idEmpresaPago) {
    this.idEmpresaPago = idEmpresaPago;
  }

  public Long getIdEmpresaPago() {
    return idEmpresaPago;
  }

  public void setPago(Double pago) {
    this.pago = pago;
  }

  public Double getPago() {
    return pago;
  }

  public void setIdCreditoNota(Long idCreditoNota) {
    this.idCreditoNota = idCreditoNota;
  }

  public Long getIdCreditoNota() {
    return idCreditoNota;
  }

	public Long getIdNotaEntrada() {
		return idNotaEntrada;
	}

	public void setIdNotaEntrada(Long idNotaEntrada) {
		this.idNotaEntrada=idNotaEntrada;
	}

  public void setIdBanco(Long idBanco) {
    this.idBanco = idBanco;
  }

  public Long getIdBanco() {
    return idBanco;
  }

  public void setReferencia(String referencia) {
    this.referencia = referencia;
  }

  public String getReferencia() {
    return referencia;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

	public Long getIdCierre() {
		return idCierre;
	}

	public void setIdCierre(Long idCierre) {
		this.idCierre = idCierre;
	}	

	public Long getIdEgreso() {
		return idEgreso;
	}

	public void setIdEgreso(Long idEgreso) {
		this.idEgreso=idEgreso;
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
  	return getIdEmpresaPago();
  }

  @Override
  public void setKey(Long key) {
  	this.idEmpresaPago = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdTipoMedioPago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresaDeuda());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresaPago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCreditoNota());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdNotaEntrada());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdBanco());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getReferencia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCierre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEgreso());
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
		regresar.put("idTipoMedioPago", getIdTipoMedioPago());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idEmpresaDeuda", getIdEmpresaDeuda());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idEmpresaPago", getIdEmpresaPago());
		regresar.put("pago", getPago());
		regresar.put("idCreditoNota", getIdCreditoNota());
		regresar.put("idNotaEntrada", getIdNotaEntrada());
		regresar.put("idBanco", getIdBanco());
		regresar.put("referencia", getReferencia());
		regresar.put("registro", getRegistro());
		regresar.put("idCierre", getIdCierre());
		regresar.put("idEgreso", getIdEgreso());
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("ejercicio", getEjercicio());
		regresar.put("orden", getOrden());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
			getIdTipoMedioPago(), getIdUsuario(), getIdEmpresaDeuda(), getObservaciones(), getIdEmpresaPago(), getPago(), getIdCreditoNota(), getIdNotaEntrada(), getIdBanco(), getReferencia(), getRegistro(), getIdCierre(), getIdEgreso(), getConsecutivo(), getOrden(), getEjercicio()
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
    regresar.append("idEmpresaPago~");
    regresar.append(getIdEmpresaPago());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdEmpresaPago());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticEmpresasPagosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdEmpresaPago()!= null && getIdEmpresaPago()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticEmpresasPagosDto other = (TcManticEmpresasPagosDto) obj;
    if (getIdEmpresaPago() != other.idEmpresaPago && (getIdEmpresaPago() == null || !getIdEmpresaPago().equals(other.idEmpresaPago))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdEmpresaPago() != null ? getIdEmpresaPago().hashCode() : 0);
    return hash;
  }
}