package mx.org.kaana.kalan.db.dto;

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
@Table(name="tc_kalan_mensajes_detalle")
public class TcKalanMensajesDetalleDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_mensaje_detalle")
  private Long idMensajeDetalle;
  @Column (name="id_cliente")
  private Long idCliente;
  @Column (name="id_mensaje")
  private Long idMensaje;
  @Column (name="celular")
  private String celular;
  @Column (name="registro")
  private Timestamp registro;

  public TcKalanMensajesDetalleDto() {
    this(new Long(-1L));
  }

  public TcKalanMensajesDetalleDto(Long key) {
    this(new Long(-1L), null, null, null);
    setKey(key);
  }

  public TcKalanMensajesDetalleDto(Long idMensajeDetalle, Long idCliente, Long idMensaje, String celular) {
    setIdMensajeDetalle(idMensajeDetalle);
    setIdCliente(idCliente);
    setIdMensaje(idMensaje);
    setCelular(celular);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdMensajeDetalle(Long idMensajeDetalle) {
    this.idMensajeDetalle = idMensajeDetalle;
  }

  public Long getIdMensajeDetalle() {
    return idMensajeDetalle;
  }

  public void setIdCliente(Long idCliente) {
    this.idCliente = idCliente;
  }

  public Long getIdCliente() {
    return idCliente;
  }

  public void setIdMensaje(Long idMensaje) {
    this.idMensaje = idMensaje;
  }

  public Long getIdMensaje() {
    return idMensaje;
  }

  public void setCelular(String celular) {
    this.celular = celular;
  }

  public String getCelular() {
    return celular;
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
  	return getIdMensajeDetalle();
  }

  @Override
  public void setKey(Long key) {
  	this.idMensajeDetalle = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdMensajeDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCliente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMensaje());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCelular());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idMensajeDetalle", getIdMensajeDetalle());
		regresar.put("idCliente", getIdCliente());
		regresar.put("idMensaje", getIdMensaje());
		regresar.put("celular", getCelular());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdMensajeDetalle(), getIdCliente(), getIdMensaje(), getCelular(), getRegistro()
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
    regresar.append("idMensajeDetalle~");
    regresar.append(getIdMensajeDetalle());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdMensajeDetalle());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcKalanMensajesDetalleDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdMensajeDetalle()!= null && getIdMensajeDetalle()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcKalanMensajesDetalleDto other = (TcKalanMensajesDetalleDto) obj;
    if (getIdMensajeDetalle() != other.idMensajeDetalle && (getIdMensajeDetalle() == null || !getIdMensajeDetalle().equals(other.idMensajeDetalle))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdMensajeDetalle() != null ? getIdMensajeDetalle().hashCode() : 0);
    return hash;
  }

}


