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
@Table(name="tc_janal_ayudas")
public class TcJanalAyudasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="CLAVE")
  private String clave;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idAyuda_sequence")
  //@SequenceGenerator(name="idAyuda_sequence",sequenceName="SEQ_TC_KAJOOL_ayudas" , allocationSize=1 )
  @Column (name="ID_AYUDA")
  private Long idAyuda;
  @Column (name="DESCRIPCION")
  private String descripcion;

  public TcJanalAyudasDto() {
    this(new Long(-1L));
  }

  public TcJanalAyudasDto(Long key) {
    this(null, null, null);
    setKey(key);
  }

  public TcJanalAyudasDto(String clave, Long idAyuda, String descripcion) {
    setClave(clave);
    setIdAyuda(idAyuda);
    setDescripcion(descripcion);
  }
	
  public void setClave(String clave) {
    this.clave = clave;
  }

  public String getClave() {
    return clave;
  }

  public void setIdAyuda(Long idAyuda) {
    this.idAyuda = idAyuda;
  }

  public Long getIdAyuda() {
    return idAyuda;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
	}

  @Transient
  @Override
  public Long getKey() {
  	return getIdAyuda();
  }

  @Override
  public void setKey(Long key) {
  	this.idAyuda = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getClave());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAyuda());
		regresar.append(Constantes.SEPARADOR);		
		regresar.append(getDescripcion());		
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("clave", getClave());
		regresar.put("idAyuda", getIdAyuda());		
		regresar.put("descripcion", getDescripcion());		
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getClave(), getIdAyuda(), getDescripcion()
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
    regresar.append("idAyuda~");
    regresar.append(getIdAyuda());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdAyuda());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcJanalAyudasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdAyuda()!= null && getIdAyuda()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcJanalAyudasDto other = (TcJanalAyudasDto) obj;
    if (getIdAyuda() != other.idAyuda && (getIdAyuda() == null || !getIdAyuda().equals(other.idAyuda))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdAyuda() != null ? getIdAyuda().hashCode() : 0);
    return hash;
  }

}


