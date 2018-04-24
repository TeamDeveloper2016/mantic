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
@Table(name="tr_mantic_cliente_tipo_contacto")
public class TrManticClienteTipoContactoDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_cliente")
  private Long idCliente;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="valor")
  private String valor;
  @Column (name="observaciones")
  private String observaciones;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_cliente_tipo_contacto")
  private Long idClienteTipoContacto;
  @Column (name="orden")
  private Long orden;
  @Column (name="id_tipo_contacto")
  private Long idTipoContacto;
  @Column (name="registro")
  private Timestamp registro;

  public TrManticClienteTipoContactoDto() {
    this(new Long(-1L));
  }

  public TrManticClienteTipoContactoDto(Long key) {
    this(null, null, null, null, new Long(-1L), null, null);
    setKey(key);
  }

  public TrManticClienteTipoContactoDto(Long idCliente, Long idUsuario, String valor, String observaciones, Long idClienteTipoContacto, Long orden, Long idTipoContacto) {
    setIdCliente(idCliente);
    setIdUsuario(idUsuario);
    setValor(valor);
    setObservaciones(observaciones);
    setIdClienteTipoContacto(idClienteTipoContacto);
    setOrden(orden);
    setIdTipoContacto(idTipoContacto);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdCliente(Long idCliente) {
    this.idCliente = idCliente;
  }

  public Long getIdCliente() {
    return idCliente;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setValor(String valor) {
    this.valor = valor;
  }

  public String getValor() {
    return valor;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setIdClienteTipoContacto(Long idClienteTipoContacto) {
    this.idClienteTipoContacto = idClienteTipoContacto;
  }

  public Long getIdClienteTipoContacto() {
    return idClienteTipoContacto;
  }

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
  }

  public void setIdTipoContacto(Long idTipoContacto) {
    this.idTipoContacto = idTipoContacto;
  }

  public Long getIdTipoContacto() {
    return idTipoContacto;
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
  	return getIdClienteTipoContacto();
  }

  @Override
  public void setKey(Long key) {
  	this.idClienteTipoContacto = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdCliente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getValor());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdClienteTipoContacto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoContacto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idCliente", getIdCliente());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("valor", getValor());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idClienteTipoContacto", getIdClienteTipoContacto());
		regresar.put("orden", getOrden());
		regresar.put("idTipoContacto", getIdTipoContacto());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdCliente(), getIdUsuario(), getValor(), getObservaciones(), getIdClienteTipoContacto(), getOrden(), getIdTipoContacto(), getRegistro()
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
    regresar.append("idClienteTipoContacto~");
    regresar.append(getIdClienteTipoContacto());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdClienteTipoContacto());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrManticClienteTipoContactoDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdClienteTipoContacto()!= null && getIdClienteTipoContacto()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrManticClienteTipoContactoDto other = (TrManticClienteTipoContactoDto) obj;
    if (getIdClienteTipoContacto() != other.idClienteTipoContacto && (getIdClienteTipoContacto() == null || !getIdClienteTipoContacto().equals(other.idClienteTipoContacto))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdClienteTipoContacto() != null ? getIdClienteTipoContacto().hashCode() : 0);
    return hash;
  }

}


