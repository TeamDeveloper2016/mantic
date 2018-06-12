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
@Table(name="tc_mantic_clientes_pagos")
public class TcManticClientesPagosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_cliente")
  private Long idCliente;
  @Column (name="observaciiones")
  private String observaciiones;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_cliente_pago")
  private Long idClientePago;
  @Column (name="saldo")
  private Double saldo;
  @Column (name="id_venta")
  private Long idVenta;
  @Column (name="importe")
  private Double importe;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticClientesPagosDto() {
    this(new Long(-1L));
  }

  public TcManticClientesPagosDto(Long key) {
    this(null, null, null, new Long(-1L), null, null, null);
    setKey(key);
  }

  public TcManticClientesPagosDto(Long idCliente, String observaciiones, Long idUsuario, Long idClientePago, Double saldo, Long idVenta, Double importe) {
    setIdCliente(idCliente);
    setObservaciiones(observaciiones);
    setIdUsuario(idUsuario);
    setIdClientePago(idClientePago);
    setSaldo(saldo);
    setIdVenta(idVenta);
    setImporte(importe);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdCliente(Long idCliente) {
    this.idCliente = idCliente;
  }

  public Long getIdCliente() {
    return idCliente;
  }

  public void setObservaciiones(String observaciiones) {
    this.observaciiones = observaciiones;
  }

  public String getObservaciiones() {
    return observaciiones;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdClientePago(Long idClientePago) {
    this.idClientePago = idClientePago;
  }

  public Long getIdClientePago() {
    return idClientePago;
  }

  public void setSaldo(Double saldo) {
    this.saldo = saldo;
  }

  public Double getSaldo() {
    return saldo;
  }

  public void setIdVenta(Long idVenta) {
    this.idVenta = idVenta;
  }

  public Long getIdVenta() {
    return idVenta;
  }

  public void setImporte(Double importe) {
    this.importe = importe;
  }

  public Double getImporte() {
    return importe;
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
		regresar.append(getIdCliente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciiones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdClientePago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSaldo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVenta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporte());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idCliente", getIdCliente());
		regresar.put("observaciiones", getObservaciiones());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idClientePago", getIdClientePago());
		regresar.put("saldo", getSaldo());
		regresar.put("idVenta", getIdVenta());
		regresar.put("importe", getImporte());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdCliente(), getObservaciiones(), getIdUsuario(), getIdClientePago(), getSaldo(), getIdVenta(), getImporte(), getRegistro()
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


