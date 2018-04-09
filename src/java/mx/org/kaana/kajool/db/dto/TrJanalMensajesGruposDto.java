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
@Table(name="tr_janal_mensajes_grupos")
public class TrJanalMensajesGruposDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="ID_USUARIO")
  private Long idUsuario;
  @Column (name="ID_MENSAJE")
  private Long idMensaje;
  @Column (name="REGISTRO")
  private Timestamp registro;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idMensajeGrupo_sequence")
  //@SequenceGenerator(name="idMensajeGrupo_sequence",sequenceName="SEQ_TR_JANAL_MENSAJES_GRUPOS" , allocationSize=1 )
  @Column (name="ID_MENSAJE_GRUPO")
  private Long idMensajeGrupo;
  @Column (name="ID_GRUPO")
  private Long idGrupo;

  public TrJanalMensajesGruposDto() {
    this(new Long(-1L));
  }

  public TrJanalMensajesGruposDto(Long key) {
    this(null, null, new Long(-1L), null);
    setKey(key);
  }

  public TrJanalMensajesGruposDto(Long idUsuario, Long idMensaje, Long idMensajeGrupo, Long idGrupo) {
    setIdUsuario(idUsuario);
    setIdMensaje(idMensaje);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setIdMensajeGrupo(idMensajeGrupo);
    setIdGrupo(idGrupo);
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

  public void setIdMensajeGrupo(Long idMensajeGrupo) {
    this.idMensajeGrupo = idMensajeGrupo;
  }

  public Long getIdMensajeGrupo() {
    return idMensajeGrupo;
  }

  public void setIdGrupo(Long idGrupo) {
    this.idGrupo = idGrupo;
  }

  public Long getIdGrupo() {
    return idGrupo;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdMensajeGrupo();
  }

  @Override
  public void setKey(Long key) {
  	this.idMensajeGrupo = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMensaje());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMensajeGrupo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdGrupo());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idMensaje", getIdMensaje());
		regresar.put("registro", getRegistro());
		regresar.put("idMensajeGrupo", getIdMensajeGrupo());
		regresar.put("idGrupo", getIdGrupo());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdUsuario(), getIdMensaje(), getRegistro(), getIdMensajeGrupo(), getIdGrupo()
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
    regresar.append("idMensajeGrupo~");
    regresar.append(getIdMensajeGrupo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdMensajeGrupo());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrJanalMensajesGruposDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdMensajeGrupo()!= null && getIdMensajeGrupo()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrJanalMensajesGruposDto other = (TrJanalMensajesGruposDto) obj;
    if (getIdMensajeGrupo() != other.idMensajeGrupo && (getIdMensajeGrupo() == null || !getIdMensajeGrupo().equals(other.idMensajeGrupo))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdMensajeGrupo() != null ? getIdMensajeGrupo().hashCode() : 0);
    return hash;
  }

}


