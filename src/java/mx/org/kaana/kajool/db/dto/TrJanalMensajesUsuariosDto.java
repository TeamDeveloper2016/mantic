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
@Table(name="tr_janal_mensajes_usuarios")
public class TrJanalMensajesUsuariosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="ID_USUARIO_MODIFICA")
  private Long idUsuarioModifica;
  @Column (name="ID_BOOLEANO")
  private Long idBooleano;
  @Column (name="ID_USUARIO")
  private Long idUsuario;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idMensajeUsuario_sequence")
  //@SequenceGenerator(name="idMensajeUsuario_sequence",sequenceName="SEQ_TR_JANAL_MENSAJES_USUARIOS" , allocationSize=1 )
  @Column (name="ID_MENSAJE_USUARIO")
  private Long idMensajeUsuario;
  @Column (name="ID_MENSAJE")
  private Long idMensaje;
  @Column (name="REGISTRO")
  private Timestamp registro;

  public TrJanalMensajesUsuariosDto() {
    this(new Long(-1L));
  }

  public TrJanalMensajesUsuariosDto(Long key) {
    this(null, null, null, new Long(-1L), null);
    setKey(key);
  }

  public TrJanalMensajesUsuariosDto(Long idUsuarioModifica, Long idBooleano, Long idUsuario, Long idMensajeUsuario, Long idMensaje) {
    setIdUsuarioModifica(idUsuarioModifica);
    setIdBooleano(idBooleano);
    setIdUsuario(idUsuario);
    setIdMensajeUsuario(idMensajeUsuario);
    setIdMensaje(idMensaje);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdUsuarioModifica(Long idUsuarioModifica) {
    this.idUsuarioModifica = idUsuarioModifica;
  }

  public Long getIdUsuarioModifica() {
    return idUsuarioModifica;
  }

  public void setIdBooleano(Long idBooleano) {
    this.idBooleano = idBooleano;
  }

  public Long getIdBooleano() {
    return idBooleano;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdMensajeUsuario(Long idMensajeUsuario) {
    this.idMensajeUsuario = idMensajeUsuario;
  }

  public Long getIdMensajeUsuario() {
    return idMensajeUsuario;
  }

  public void setIdMensaje(Long idMensaje) {
    this.idMensaje = idMensaje;
  }

  public Long getIdMensaje() {
    return idMensaje;
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
  	return getIdMensajeUsuario();
  }

  @Override
  public void setKey(Long key) {
  	this.idMensajeUsuario = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdUsuarioModifica());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdBooleano());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMensajeUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMensaje());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idUsuarioModifica", getIdUsuarioModifica());
		regresar.put("idBooleano", getIdBooleano());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idMensajeUsuario", getIdMensajeUsuario());
		regresar.put("idMensaje", getIdMensaje());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdUsuarioModifica(), getIdBooleano(), getIdUsuario(), getIdMensajeUsuario(), getIdMensaje(), getRegistro()
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
    regresar.append("idMensajeUsuario~");
    regresar.append(getIdMensajeUsuario());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdMensajeUsuario());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrJanalMensajesUsuariosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdMensajeUsuario()!= null && getIdMensajeUsuario()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrJanalMensajesUsuariosDto other = (TrJanalMensajesUsuariosDto) obj;
    if (getIdMensajeUsuario() != other.idMensajeUsuario && (getIdMensajeUsuario() == null || !getIdMensajeUsuario().equals(other.idMensajeUsuario))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdMensajeUsuario() != null ? getIdMensajeUsuario().hashCode() : 0);
    return hash;
  }

}


