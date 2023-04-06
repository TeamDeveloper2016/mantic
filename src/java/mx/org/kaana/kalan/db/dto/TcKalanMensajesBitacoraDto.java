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
@Table(name="tc_kalan_mensajes_bitacora")
public class TcKalanMensajesBitacoraDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_mensaje_bitacora")
  private Long idMensajeBitacora;
  @Column (name="id_mensaje")
  private Long idMensaje;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_mensaje_estatus")
  private Long idMensajeEstatus;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="registro")
  private Timestamp registro;

  public TcKalanMensajesBitacoraDto() {
    this(new Long(-1L));
  }

  public TcKalanMensajesBitacoraDto(Long key) {
    this(new Long(-1L), null, null, null, null);
    setKey(key);
  }

  public TcKalanMensajesBitacoraDto(Long idMensajeBitacora, Long idMensaje, Long idUsuario, Long idMensajeEstatus, String observaciones) {
    setIdMensajeBitacora(idMensajeBitacora);
    setIdMensaje(idMensaje);
    setIdUsuario(idUsuario);
    setIdMensajeEstatus(idMensajeEstatus);
    setObservaciones(observaciones);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdMensajeBitacora(Long idMensajeBitacora) {
    this.idMensajeBitacora = idMensajeBitacora;
  }

  public Long getIdMensajeBitacora() {
    return idMensajeBitacora;
  }

  public void setIdMensaje(Long idMensaje) {
    this.idMensaje = idMensaje;
  }

  public Long getIdMensaje() {
    return idMensaje;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdMensajeEstatus(Long idMensajeEstatus) {
    this.idMensajeEstatus = idMensajeEstatus;
  }

  public Long getIdMensajeEstatus() {
    return idMensajeEstatus;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
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
  	return getIdMensajeBitacora();
  }

  @Override
  public void setKey(Long key) {
  	this.idMensajeBitacora = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdMensajeBitacora());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMensaje());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMensajeEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idMensajeBitacora", getIdMensajeBitacora());
		regresar.put("idMensaje", getIdMensaje());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idMensajeEstatus", getIdMensajeEstatus());
		regresar.put("observaciones", getObservaciones());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdMensajeBitacora(), getIdMensaje(), getIdUsuario(), getIdMensajeEstatus(), getObservaciones(), getRegistro()
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
    regresar.append("idMensajeBitacora~");
    regresar.append(getIdMensajeBitacora());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdMensajeBitacora());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcKalanMensajesBitacoraDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdMensajeBitacora()!= null && getIdMensajeBitacora()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcKalanMensajesBitacoraDto other = (TcKalanMensajesBitacoraDto) obj;
    if (getIdMensajeBitacora() != other.idMensajeBitacora && (getIdMensajeBitacora() == null || !getIdMensajeBitacora().equals(other.idMensajeBitacora))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdMensajeBitacora() != null ? getIdMensajeBitacora().hashCode() : 0);
    return hash;
  }

}


