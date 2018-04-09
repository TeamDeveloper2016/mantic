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
@Table(name="tc_janal_grupos")
public class TcJanalGruposDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="CLAVE")
  private String clave;
  @Column (name="ID_USUARIO")
  private Long idUsuario;
  @Column (name="DESCRIPCION")
  private String descripcion;
  @Column (name="REGISTRO")
  private Timestamp registro;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idGrupo_sequence")
  //@SequenceGenerator(name="idGrupo_sequence",sequenceName="SEQ_TC_KAJOOL_grupos" , allocationSize=1 )
  @Column (name="ID_GRUPO")
  private Long idGrupo;

  public TcJanalGruposDto() {
    this(new Long(-1L));
  }

  public TcJanalGruposDto(Long key) {
    this(null, null, null, null);
    setKey(key);
  }

  public TcJanalGruposDto(String clave, Long idUsuario, String descripcion, Long idGrupo) {
    setClave(clave);
    setIdUsuario(idUsuario);
    setDescripcion(descripcion);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setIdGrupo(idGrupo);
  }
	
  public void setClave(String clave) {
    this.clave = clave;
  }

  public String getClave() {
    return clave;
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

  public void setIdGrupo(Long idGrupo) {
    this.idGrupo = idGrupo;
  }

  public Long getIdGrupo() {
    return idGrupo;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdGrupo();
  }

  @Override
  public void setKey(Long key) {
  	this.idGrupo = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getClave());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdGrupo());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("clave", getClave());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("descripcion", getDescripcion());
		regresar.put("registro", getRegistro());
		regresar.put("idGrupo", getIdGrupo());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getClave(), getIdUsuario(), getDescripcion(), getRegistro(), getIdGrupo()
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
    regresar.append("idGrupo~");
    regresar.append(getIdGrupo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdGrupo());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcJanalGruposDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdGrupo()!= null && getIdGrupo()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcJanalGruposDto other = (TcJanalGruposDto) obj;
    if (getIdGrupo() != other.idGrupo && (getIdGrupo() == null || !getIdGrupo().equals(other.idGrupo))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdGrupo() != null ? getIdGrupo().hashCode() : 0);
    return hash;
  }

}
