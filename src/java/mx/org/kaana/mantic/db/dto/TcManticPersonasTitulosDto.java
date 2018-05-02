package mx.org.kaana.mantic.db.dto;

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
@Table(name="tc_mantic_personas_titulos")
public class TcManticPersonasTitulosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="descrpcion")
  private String descrpcion;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_persona_titulo")
  private Long idPersonaTitulo;
  @Column (name="nombre")
  private String nombre;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticPersonasTitulosDto() {
    this(new Long(-1L));
  }

  public TcManticPersonasTitulosDto(Long key) {
    this(null, new Long(-1L), null);
    setKey(key);
  }

  public TcManticPersonasTitulosDto(String descrpcion, Long idPersonaTitulo, String nombre) {
    setDescrpcion(descrpcion);
    setIdPersonaTitulo(idPersonaTitulo);
    setNombre(nombre);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setDescrpcion(String descrpcion) {
    this.descrpcion = descrpcion;
  }

  public String getDescrpcion() {
    return descrpcion;
  }

  public void setIdPersonaTitulo(Long idPersonaTitulo) {
    this.idPersonaTitulo = idPersonaTitulo;
  }

  public Long getIdPersonaTitulo() {
    return idPersonaTitulo;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
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
  	return getIdPersonaTitulo();
  }

  @Override
  public void setKey(Long key) {
  	this.idPersonaTitulo = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDescrpcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPersonaTitulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("descrpcion", getDescrpcion());
		regresar.put("idPersonaTitulo", getIdPersonaTitulo());
		regresar.put("nombre", getNombre());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getDescrpcion(), getIdPersonaTitulo(), getNombre(), getRegistro()
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
    regresar.append("idPersonaTitulo~");
    regresar.append(getIdPersonaTitulo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdPersonaTitulo());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticPersonasTitulosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdPersonaTitulo()!= null && getIdPersonaTitulo()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticPersonasTitulosDto other = (TcManticPersonasTitulosDto) obj;
    if (getIdPersonaTitulo() != other.idPersonaTitulo && (getIdPersonaTitulo() == null || !getIdPersonaTitulo().equals(other.idPersonaTitulo))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdPersonaTitulo() != null ? getIdPersonaTitulo().hashCode() : 0);
    return hash;
  }

}


