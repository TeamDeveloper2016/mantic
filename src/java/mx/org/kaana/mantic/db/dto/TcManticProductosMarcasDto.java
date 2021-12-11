package mx.org.kaana.mantic.db.dto;

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

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/10/2016
 *@time 11:58:22 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Entity
@Table(name="tc_mantic_productos_marcas")
public class TcManticProductosMarcasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_producto_marca_archivo")
  private Long idProductoMarcaArchivo;
  @Column (name="descripcion")
  private String descripcion;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_producto_marca")
  private Long idProductoMarca;
  @Column (name="nombre")
  private String nombre;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticProductosMarcasDto() {
    this(new Long(-1L));
  }

  public TcManticProductosMarcasDto(Long key) {
    this(null, null, null, new Long(-1L), null);
    setKey(key);
  }

  public TcManticProductosMarcasDto(Long idProductoMarcaArchivo, String descripcion, Long idUsuario, Long idProductoMarca, String nombre) {
    setIdProductoMarcaArchivo(idProductoMarcaArchivo);
    setDescripcion(descripcion);
    setIdUsuario(idUsuario);
    setIdProductoMarca(idProductoMarca);
    setNombre(nombre);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdProductoMarcaArchivo(Long idProductoMarcaArchivo) {
    this.idProductoMarcaArchivo = idProductoMarcaArchivo;
  }

  public Long getIdProductoMarcaArchivo() {
    return idProductoMarcaArchivo;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdProductoMarca(Long idProductoMarca) {
    this.idProductoMarca = idProductoMarca;
  }

  public Long getIdProductoMarca() {
    return idProductoMarca;
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
  	return getIdProductoMarca();
  }

  @Override
  public void setKey(Long key) {
  	this.idProductoMarca = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdProductoMarcaArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdProductoMarca());
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
		regresar.put("idProductoMarcaArchivo", getIdProductoMarcaArchivo());
		regresar.put("descripcion", getDescripcion());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idProductoMarca", getIdProductoMarca());
		regresar.put("nombre", getNombre());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
      getIdProductoMarcaArchivo(), getDescripcion(), getIdUsuario(), getIdProductoMarca(), getNombre(), getRegistro()
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
    regresar.append("idProductoMarca~");
    regresar.append(getIdProductoMarca());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdProductoMarca());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticProductosMarcasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdProductoMarca()!= null && getIdProductoMarca()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticProductosMarcasDto other = (TcManticProductosMarcasDto) obj;
    if (getIdProductoMarca() != other.idProductoMarca && (getIdProductoMarca() == null || !getIdProductoMarca().equals(other.idProductoMarca))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdProductoMarca() != null ? getIdProductoMarca().hashCode() : 0);
    return hash;
  }

}


