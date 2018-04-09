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
@Table(name="tc_janal_contador_ayudas")
public class TcJanalContadorAyudasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="ID_AYUDA")
  private Long idAyuda;
  @Column (name="ID_USUARIO")
  private Long idUsuario;
  @Column (name="REGISTRO")
  private Timestamp registro;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idContarAyuda_sequence")
  //@SequenceGenerator(name="idContarAyuda_sequence",sequenceName="SEQ_TC_KAJOOL_contador_ayudas" , allocationSize=1 )
  @Column (name="ID_CONTAR_AYUDA")
  private Long idContarAyuda;

  public TcJanalContadorAyudasDto() {
    this(new Long(-1L));
  }

  public TcJanalContadorAyudasDto(Long key) {
    this(null, null, null);
    setKey(key);
  }

  public TcJanalContadorAyudasDto(Long idAyuda, Long idUsuario, Long idContarAyuda) {
    setIdAyuda(idAyuda);
    setIdUsuario(idUsuario);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setIdContarAyuda(idContarAyuda);
  }
	
  public void setIdAyuda(Long idAyuda) {
    this.idAyuda = idAyuda;
  }

  public Long getIdAyuda() {
    return idAyuda;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setIdContarAyuda(Long idContarAyuda) {
    this.idContarAyuda = idContarAyuda;
  }

  public Long getIdContarAyuda() {
    return idContarAyuda;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdContarAyuda();
  }

  @Override
  public void setKey(Long key) {
  	this.idContarAyuda = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdAyuda());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdContarAyuda());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idAyuda", getIdAyuda());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("registro", getRegistro());
		regresar.put("idContarAyuda", getIdContarAyuda());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdAyuda(), getIdUsuario(), getRegistro(), getIdContarAyuda()
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
    regresar.append("idContarAyuda~");
    regresar.append(getIdContarAyuda());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdContarAyuda());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcJanalContadorAyudasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdContarAyuda()!= null && getIdContarAyuda()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcJanalContadorAyudasDto other = (TcJanalContadorAyudasDto) obj;
    if (getIdContarAyuda() != other.idContarAyuda && (getIdContarAyuda() == null || !getIdContarAyuda().equals(other.idContarAyuda))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdContarAyuda() != null ? getIdContarAyuda().hashCode() : 0);
    return hash;
  }

}


