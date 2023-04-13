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
@Table(name="tc_mantic_tipos_clientes")
public class TcManticTiposClientesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="descripcion")
  private String descripcion;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_tipo_cliente")
  private Long idTipoCliente;
  @Column (name="nombre")
  private String nombre;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticTiposClientesDto() {
    this(new Long(-1L));
  }

  public TcManticTiposClientesDto(Long key) {
    this(null, new Long(-1L), null);
    setKey(key);
  }

  public TcManticTiposClientesDto(String descripcion, Long idTipoCliente, String nombre) {
    setDescripcion(descripcion);
    setIdTipoCliente(idTipoCliente);
    setNombre(nombre);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setIdTipoCliente(Long idTipoCliente) {
    this.idTipoCliente = idTipoCliente;
  }

  public Long getIdTipoCliente() {
    return idTipoCliente;
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
  	return getIdTipoCliente();
  }

  @Override
  public void setKey(Long key) {
  	this.idTipoCliente = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoCliente());
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
		regresar.put("descripcion", getDescripcion());
		regresar.put("idTipoCliente", getIdTipoCliente());
		regresar.put("nombre", getNombre());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getDescripcion(), getIdTipoCliente(), getNombre(), getRegistro()
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
    regresar.append("idTipoCliente~");
    regresar.append(getIdTipoCliente());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdTipoCliente());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticTiposClientesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdTipoCliente()!= null && getIdTipoCliente()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticTiposClientesDto other = (TcManticTiposClientesDto) obj;
    if (getIdTipoCliente() != other.idTipoCliente && (getIdTipoCliente() == null || !getIdTipoCliente().equals(other.idTipoCliente))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdTipoCliente() != null ? getIdTipoCliente().hashCode() : 0);
    return hash;
  }

}


