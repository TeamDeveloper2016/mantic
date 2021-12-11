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
@Table(name="tc_productos_marcas_archivos")
public class TcManticProductosMarcasArchivosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_producto_marca_archivo")
  private Long idProductoMarcaArchivo;
  @Column (name="archivo")
  private String archivo;
  @Column (name="tamanio")
  private Long tamanio;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="alias")
  private String alias;
  @Column (name="id_tipo_imagen")
  private Long idTipoImagen;
  @Column (name="nombre")
  private String nombre;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticProductosMarcasArchivosDto() {
    this(new Long(-1L));
  }

  public TcManticProductosMarcasArchivosDto(Long key) {
    this(new Long(-1L), null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticProductosMarcasArchivosDto(Long idProductoMarcaArchivo, String archivo, Long tamanio, Long idUsuario, String alias, Long idTipoImagen, String nombre) {
    setIdProductoMarcaArchivo(idProductoMarcaArchivo);
    setArchivo(archivo);
    setTamanio(tamanio);
    setIdUsuario(idUsuario);
    setAlias(alias);
    setIdTipoImagen(idTipoImagen);
    setNombre(nombre);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdProductoMarcaArchivo(Long idProductoMarcaArchivo) {
    this.idProductoMarcaArchivo = idProductoMarcaArchivo;
  }

  public Long getIdProductoMarcaArchivo() {
    return idProductoMarcaArchivo;
  }

  public void setArchivo(String archivo) {
    this.archivo = archivo;
  }

  public String getArchivo() {
    return archivo;
  }

  public void setTamanio(Long tamanio) {
    this.tamanio = tamanio;
  }

  public Long getTamanio() {
    return tamanio;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String getAlias() {
    return alias;
  }

  public void setIdTipoImagen(Long idTipoImagen) {
    this.idTipoImagen = idTipoImagen;
  }

  public Long getIdTipoImagen() {
    return idTipoImagen;
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
  	return getIdProductoMarcaArchivo();
  }

  @Override
  public void setKey(Long key) {
  	this.idProductoMarcaArchivo = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdProductoMarcaArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTamanio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAlias());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoImagen());
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
		regresar.put("archivo", getArchivo());
		regresar.put("tamanio", getTamanio());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("alias", getAlias());
		regresar.put("idTipoImagen", getIdTipoImagen());
		regresar.put("nombre", getNombre());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
      getIdProductoMarcaArchivo(), getArchivo(), getTamanio(), getIdUsuario(), getAlias(), getIdTipoImagen(), getNombre(), getRegistro()
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
    regresar.append("idProductoMarcaArchivo~");
    regresar.append(getIdProductoMarcaArchivo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdProductoMarcaArchivo());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticProductosMarcasArchivosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdProductoMarcaArchivo()!= null && getIdProductoMarcaArchivo()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticProductosMarcasArchivosDto other = (TcManticProductosMarcasArchivosDto) obj;
    if (getIdProductoMarcaArchivo() != other.idProductoMarcaArchivo && (getIdProductoMarcaArchivo() == null || !getIdProductoMarcaArchivo().equals(other.idProductoMarcaArchivo))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdProductoMarcaArchivo() != null ? getIdProductoMarcaArchivo().hashCode() : 0);
    return hash;
  }

}


