package mx.org.kaana.kajool.db.dto;

import java.io.Serializable;
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
@Table(name="tc_janal_modulos")
public class TcJanalModulosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="DESCRIPCION")
  private String descripcion;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idModulo_sequence")
  //@SequenceGenerator(name="idModulo_sequence",sequenceName="SEQ_TC_JANAL_MODULOS" , allocationSize=1 )
  @Column (name="ID_MODULO")
  private Long idModulo;

  public TcJanalModulosDto() {
    this(new Long(-1L));
  }

  public TcJanalModulosDto(Long key) {
    this(null, new Long(-1L));
    setKey(key);
  }

  public TcJanalModulosDto(String descripcion, Long idModulo) {
    setDescripcion(descripcion);
    setIdModulo(idModulo);
  }
	
  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setIdModulo(Long idModulo) {
    this.idModulo = idModulo;
  }

  public Long getIdModulo() {
    return idModulo;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdModulo();
  }

  @Override
  public void setKey(Long key) {
  	this.idModulo = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdModulo());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("descripcion", getDescripcion());
		regresar.put("idModulo", getIdModulo());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getDescripcion(), getIdModulo()
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
    regresar.append("idModulo~");
    regresar.append(getIdModulo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdModulo());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcJanalModulosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdModulo()!= null && getIdModulo()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcJanalModulosDto other = (TcJanalModulosDto) obj;
    if (getIdModulo() != other.idModulo && (getIdModulo() == null || !getIdModulo().equals(other.idModulo))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdModulo() != null ? getIdModulo().hashCode() : 0);
    return hash;
  }

}


