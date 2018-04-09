package mx.org.kaana.kajool.db.dto;

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

@Entity
@Table(name="tc_janal_tipos_mensajes")
public class TcJanalTiposMensajesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idTipoMensaje_sequence")
  //@SequenceGenerator(name="idTipoMensaje_sequence",sequenceName="SEQ_TC_JANAL_TIPOS_MENSAJES" , allocationSize=1 )
  @Column (name="ID_TIPO_MENSAJE")
  private Long idTipoMensaje;
  @Column (name="ID_USUARIO")
  private Long idUsuario;
  @Column (name="DESCRIPCION")
  private String descripcion;
  @Column (name="REGISTRO")
  private Timestamp registro;

  public TcJanalTiposMensajesDto() {
    this(new Long(-1L));
  }

  public TcJanalTiposMensajesDto(Long key) {
    this(new Long(-1L), null, null);
    setKey(key);
  }

  public TcJanalTiposMensajesDto(Long idTipoMensaje, Long idUsuario, String descripcion) {
    setIdTipoMensaje(idTipoMensaje);
    setIdUsuario(idUsuario);
    setDescripcion(descripcion);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdTipoMensaje(Long idTipoMensaje) {
    this.idTipoMensaje = idTipoMensaje;
  }

  public Long getIdTipoMensaje() {
    return idTipoMensaje;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
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
  	return getIdTipoMensaje();
  }

  @Override
  public void setKey(Long key) {
  	this.idTipoMensaje = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdTipoMensaje());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idTipoMensaje", getIdTipoMensaje());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("descripcion", getDescripcion());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdTipoMensaje(), getIdUsuario(), getDescripcion(), getRegistro()
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
    regresar.append("idTipoMensaje~");
    regresar.append(getIdTipoMensaje());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdTipoMensaje());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcJanalTiposMensajesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdTipoMensaje()!= null && getIdTipoMensaje()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcJanalTiposMensajesDto other = (TcJanalTiposMensajesDto) obj;
    if (getIdTipoMensaje() != other.idTipoMensaje && (getIdTipoMensaje() == null || !getIdTipoMensaje().equals(other.idTipoMensaje))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdTipoMensaje() != null ? getIdTipoMensaje().hashCode() : 0);
    return hash;
  }

}


