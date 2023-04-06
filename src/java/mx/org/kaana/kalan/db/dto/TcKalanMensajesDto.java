package mx.org.kaana.kalan.db.dto;

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
@Table(name="tc_kalan_mensajes")
public class TcKalanMensajesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="consecutivo")
  private String consecutivo;
  @Column (name="descripcion")
  private String descripcion;
  @Column (name="cuando")
  private Timestamp cuando;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_mensaje")
  private Long idMensaje;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="aplicado")
  private Timestamp aplicado;
  @Column (name="orden")
  private Long orden;
  @Column (name="ejercicio")
  private Long ejercicio;
  @Column (name="id_mensaje_estatus")
  private Long idMensajeEstatus;
  @Column (name="registro")
  private Timestamp registro;

  public TcKalanMensajesDto() {
    this(new Long(-1L));
  }

  public TcKalanMensajesDto(Long key) {
    this(null, null, new Timestamp(Calendar.getInstance().getTimeInMillis()), new Long(-1L), null, new Timestamp(Calendar.getInstance().getTimeInMillis()), null, null, 1L);
    setKey(key);
  }

  public TcKalanMensajesDto(String consecutivo, String descripcion, Timestamp cuando, Long idMensaje, Long idUsuario, Timestamp aplicado, Long orden, Long ejercicio, Long idMensajeEstatus) {
    setConsecutivo(consecutivo);
    setDescripcion(descripcion);
    setCuando(cuando);
    setIdMensaje(idMensaje);
    setIdUsuario(idUsuario);
    setAplicado(aplicado);
    setOrden(orden);
    setEjercicio(ejercicio);
    setIdMensajeEstatus(idMensajeEstatus);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setConsecutivo(String consecutivo) {
    this.consecutivo = consecutivo;
  }

  public String getConsecutivo() {
    return consecutivo;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setCuando(Timestamp cuando) {
    this.cuando = cuando;
  }

  public Timestamp getCuando() {
    return cuando;
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

  public void setAplicado(Timestamp aplicado) {
    this.aplicado = aplicado;
  }

  public Timestamp getAplicado() {
    return aplicado;
  }

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
  }

  public void setEjercicio(Long ejercicio) {
    this.ejercicio = ejercicio;
  }

  public Long getEjercicio() {
    return ejercicio;
  }

  public Long getIdMensajeEstatus() {
    return idMensajeEstatus;
  }

  public void setIdMensajeEstatus(Long idMensajeEstatus) {
    this.idMensajeEstatus = idMensajeEstatus;
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
  	return getIdMensaje();
  }

  @Override
  public void setKey(Long key) {
  	this.idMensaje = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCuando());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMensaje());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAplicado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEjercicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMensajeEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("descripcion", getDescripcion());
		regresar.put("cuando", getCuando());
		regresar.put("idMensaje", getIdMensaje());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("aplicado", getAplicado());
		regresar.put("orden", getOrden());
		regresar.put("ejercicio", getEjercicio());
		regresar.put("idMensajeEstatus", getIdMensajeEstatus());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getConsecutivo(), getDescripcion(), getCuando(), getIdMensaje(), getIdUsuario(), getAplicado(), getOrden(), getEjercicio(), getIdMensajeEstatus(), getRegistro()
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
    regresar.append("idMensaje~");
    regresar.append(getIdMensaje());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdMensaje());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcKalanMensajesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdMensaje()!= null && getIdMensaje()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcKalanMensajesDto other = (TcKalanMensajesDto) obj;
    if (getIdMensaje() != other.idMensaje && (getIdMensaje() == null || !getIdMensaje().equals(other.idMensaje))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdMensaje() != null ? getIdMensaje().hashCode() : 0);
    return hash;
  }

}


