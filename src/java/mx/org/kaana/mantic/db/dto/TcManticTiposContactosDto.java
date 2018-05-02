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
@Table(name="tc_mantic_tipos_contactos")
public class TcManticTiposContactosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="descrpcion")
  private String descrpcion;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_tipo_medio")
  private Long idTipoMedio;
  @Column (name="orden")
  private Long orden;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_tipo_contacto")
  private Long idTipoContacto;
  @Column (name="nombre")
  private String nombre;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticTiposContactosDto() {
    this(new Long(-1L));
  }

  public TcManticTiposContactosDto(Long key) {
    this(null, null, null, null, new Long(-1L), null);
    setKey(key);
  }

  public TcManticTiposContactosDto(String descrpcion, Long idUsuario, Long idTipoMedio, Long orden, Long idTipoContacto, String nombre) {
    setDescrpcion(descrpcion);
    setIdUsuario(idUsuario);
    setIdTipoMedio(idTipoMedio);
    setOrden(orden);
    setIdTipoContacto(idTipoContacto);
    setNombre(nombre);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setDescrpcion(String descrpcion) {
    this.descrpcion = descrpcion;
  }

  public String getDescrpcion() {
    return descrpcion;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdTipoMedio(Long idTipoMedio) {
    this.idTipoMedio = idTipoMedio;
  }

  public Long getIdTipoMedio() {
    return idTipoMedio;
  }

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
  }

  public void setIdTipoContacto(Long idTipoContacto) {
    this.idTipoContacto = idTipoContacto;
  }

  public Long getIdTipoContacto() {
    return idTipoContacto;
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
  	return getIdTipoContacto();
  }

  @Override
  public void setKey(Long key) {
  	this.idTipoContacto = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDescrpcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoMedio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoContacto());
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
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idTipoMedio", getIdTipoMedio());
		regresar.put("orden", getOrden());
		regresar.put("idTipoContacto", getIdTipoContacto());
		regresar.put("nombre", getNombre());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getDescrpcion(), getIdUsuario(), getIdTipoMedio(), getOrden(), getIdTipoContacto(), getNombre(), getRegistro()
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
    regresar.append("idTipoContacto~");
    regresar.append(getIdTipoContacto());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdTipoContacto());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticTiposContactosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdTipoContacto()!= null && getIdTipoContacto()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticTiposContactosDto other = (TcManticTiposContactosDto) obj;
    if (getIdTipoContacto() != other.idTipoContacto && (getIdTipoContacto() == null || !getIdTipoContacto().equals(other.idTipoContacto))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdTipoContacto() != null ? getIdTipoContacto().hashCode() : 0);
    return hash;
  }

}


