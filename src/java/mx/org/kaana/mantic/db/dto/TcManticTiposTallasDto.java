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
@Table(name="tc_mantic_tipos_tallas")
public class TcManticTiposTallasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_tipo_talla")
  private Long idTipoTalla;
  @Column (name="descripcion")
  private String descripcion;
  @Column (name="clave")
  private String clave;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="orden")
  private Long orden;
  @Column (name="nombre")
  private String nombre;
  @Column (name="id_tipo_articulo")
  private Long idTipoArticulo;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticTiposTallasDto() {
    this(new Long(-1L));
  }

  public TcManticTiposTallasDto(Long key) {
    this(new Long(-1L), null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticTiposTallasDto(Long idTipoTalla, String descripcion, String clave, Long idUsuario, Long orden, String nombre, Long idTipoArticulo) {
    setIdTipoTalla(idTipoTalla);
    setDescripcion(descripcion);
    setClave(clave);
    setIdUsuario(idUsuario);
    setOrden(orden);
    setNombre(nombre);
    setIdTipoArticulo(idTipoArticulo);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdTipoTalla(Long idTipoTalla) {
    this.idTipoTalla = idTipoTalla;
  }

  public Long getIdTipoTalla() {
    return idTipoTalla;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
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

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public void setIdTipoArticulo(Long idTipoArticulo) {
    this.idTipoArticulo = idTipoArticulo;
  }

  public Long getIdTipoArticulo() {
    return idTipoArticulo;
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
  	return getIdTipoTalla();
  }

  @Override
  public void setKey(Long key) {
  	this.idTipoTalla = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdTipoTalla());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getClave());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idTipoTalla", getIdTipoTalla());
		regresar.put("descripcion", getDescripcion());
		regresar.put("clave", getClave());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("orden", getOrden());
		regresar.put("nombre", getNombre());
		regresar.put("idTipoArticulo", getIdTipoArticulo());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdTipoTalla(), getDescripcion(), getClave(), getIdUsuario(), getOrden(), getNombre(), getIdTipoArticulo(), getRegistro()
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
    regresar.append("idTipoTalla~");
    regresar.append(getIdTipoTalla());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdTipoTalla());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticTiposTallasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdTipoTalla()!= null && getIdTipoTalla()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticTiposTallasDto other = (TcManticTiposTallasDto) obj;
    if (getIdTipoTalla() != other.idTipoTalla && (getIdTipoTalla() == null || !getIdTipoTalla().equals(other.idTipoTalla))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdTipoTalla() != null ? getIdTipoTalla().hashCode() : 0);
    return hash;
  }

}


