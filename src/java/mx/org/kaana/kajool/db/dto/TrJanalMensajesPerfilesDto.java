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
@Table(name="tr_janal_mensajes_perfiles")
public class TrJanalMensajesPerfilesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idMensajePerfil_sequence")
  //@SequenceGenerator(name="idMensajePerfil_sequence",sequenceName="SEQ_TR_JANAL_MENSAJES_PERFILES" , allocationSize=1 )
  @Column (name="ID_MENSAJE_PERFIL")
  private Long idMensajePerfil;
  @Column (name="ID_PERFIL")
  private Long idPerfil;
  @Column (name="ID_USUARIO")
  private Long idUsuario;
  @Column (name="ID_MENSAJE")
  private Long idMensaje;
  @Column (name="REGISTRO")
  private Timestamp registro;

  public TrJanalMensajesPerfilesDto() {
    this(new Long(-1L));
  }

  public TrJanalMensajesPerfilesDto(Long key) {
    this(new Long(-1L), null, null, null);
    setKey(key);
  }

  public TrJanalMensajesPerfilesDto(Long idMensajePerfil, Long idPerfil, Long idUsuario, Long idMensaje) {
    setIdMensajePerfil(idMensajePerfil);
    setIdPerfil(idPerfil);
    setIdUsuario(idUsuario);
    setIdMensaje(idMensaje);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdMensajePerfil(Long idMensajePerfil) {
    this.idMensajePerfil = idMensajePerfil;
  }

  public Long getIdMensajePerfil() {
    return idMensajePerfil;
  }

  public void setIdPerfil(Long idPerfil) {
    this.idPerfil = idPerfil;
  }

  public Long getIdPerfil() {
    return idPerfil;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
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
  	return getIdMensajePerfil();
  }

  @Override
  public void setKey(Long key) {
  	this.idMensajePerfil = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdMensajePerfil());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPerfil());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
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
		regresar.put("idMensajePerfil", getIdMensajePerfil());
		regresar.put("idPerfil", getIdPerfil());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idMensaje", getIdMensaje());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdMensajePerfil(), getIdPerfil(), getIdUsuario(), getIdMensaje(), getRegistro()
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
    regresar.append("idMensajePerfil~");
    regresar.append(getIdMensajePerfil());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdMensajePerfil());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrJanalMensajesPerfilesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdMensajePerfil()!= null && getIdMensajePerfil()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrJanalMensajesPerfilesDto other = (TrJanalMensajesPerfilesDto) obj;
    if (getIdMensajePerfil() != other.idMensajePerfil && (getIdMensajePerfil() == null || !getIdMensajePerfil().equals(other.idMensajePerfil))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdMensajePerfil() != null ? getIdMensajePerfil().hashCode() : 0);
    return hash;
  }

}


