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
@Table(name="tc_mantic_clientes_pagos_archivos")
public class TcManticClientesPagosArchivosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="ruta")
  private String ruta;
  @Column (name="tamanio")
  private Long tamanio;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_tipo_archivo")
  private Long idTipoArchivo;
  @Column (name="id_principal")
  private Long idPrincipal;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="id_cliente_pago")
  private Long idClientePago;
  @Column (name="alias")
  private String alias;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_cliente_pago_archivo")
  private Long idClientePagoArchivo;
  @Column (name="nombre")
  private String nombre;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticClientesPagosArchivosDto() {
    this(new Long(-1L));
  }

  public TcManticClientesPagosArchivosDto(Long key) {
    this(null, null, null, null, null, null, null, null, new Long(-1L), null);
    setKey(key);
  }

  public TcManticClientesPagosArchivosDto(String ruta, Long tamanio, Long idUsuario, Long idTipoArchivo, Long idPrincipal, String observaciones, Long idClientePago, String alias, Long idClientePagoArchivo, String nombre) {
    setRuta(ruta);
    setTamanio(tamanio);
    setIdUsuario(idUsuario);
    setIdTipoArchivo(idTipoArchivo);
    setIdPrincipal(idPrincipal);
    setObservaciones(observaciones);
    setIdClientePago(idClientePago);
    setAlias(alias);
    setIdClientePagoArchivo(idClientePagoArchivo);
    setNombre(nombre);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setRuta(String ruta) {
    this.ruta = ruta;
  }

  public String getRuta() {
    return ruta;
  }

  public void setTamanio(Long tamanio) {
    this.tamanio = tamanio;
  }

  public Long getTamanio() {
    return tamanio;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdTipoArchivo(Long idTipoArchivo) {
    this.idTipoArchivo = idTipoArchivo;
  }

  public Long getIdTipoArchivo() {
    return idTipoArchivo;
  }

  public void setIdPrincipal(Long idPrincipal) {
    this.idPrincipal = idPrincipal;
  }

  public Long getIdPrincipal() {
    return idPrincipal;
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

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String getAlias() {
    return alias;
  }

  public void setIdClientePagoArchivo(Long idClientePagoArchivo) {
    this.idClientePagoArchivo = idClientePagoArchivo;
  }

  public Long getIdClientePagoArchivo() {
    return idClientePagoArchivo;
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
  	return getIdClientePagoArchivo();
  }

  @Override
  public void setKey(Long key) {
  	this.idClientePagoArchivo = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getRuta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTamanio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPrincipal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdClientePago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAlias());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdClientePagoArchivo());
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
		regresar.put("ruta", getRuta());
		regresar.put("tamanio", getTamanio());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idTipoArchivo", getIdTipoArchivo());
		regresar.put("idPrincipal", getIdPrincipal());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idClientePago", getIdClientePago());
		regresar.put("alias", getAlias());
		regresar.put("idClientePagoArchivo", getIdClientePagoArchivo());
		regresar.put("nombre", getNombre());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getRuta(), getTamanio(), getIdUsuario(), getIdTipoArchivo(), getIdPrincipal(), getObservaciones(), getIdClientePago(), getAlias(), getIdClientePagoArchivo(), getNombre(), getRegistro()
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
    regresar.append("idClientePagoArchivo~");
    regresar.append(getIdClientePagoArchivo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdClientePagoArchivo());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticClientesPagosArchivosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdClientePagoArchivo()!= null && getIdClientePagoArchivo()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticClientesPagosArchivosDto other = (TcManticClientesPagosArchivosDto) obj;
    if (getIdClientePagoArchivo() != other.idClientePagoArchivo && (getIdClientePagoArchivo() == null || !getIdClientePagoArchivo().equals(other.idClientePagoArchivo))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdClientePagoArchivo() != null ? getIdClientePagoArchivo().hashCode() : 0);
    return hash;
  }

}


