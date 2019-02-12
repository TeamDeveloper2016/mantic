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
@Table(name="tc_mantic_imagenes")
public class TcManticImagenesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="archivo")
  private String archivo;
  @Column (name="ruta")
  private String ruta;
  @Column (name="tamanio")
  private Long tamanio;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_imagen")
  private Long idImagen;
  @Column (name="id_tipo_imagen")
  private Long idTipoImagen;
  @Column (name="nombre")
  private String nombre;
  @Column (name="alias")
  private String alias;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticImagenesDto() {
    this(new Long(-1L));
  }

  public TcManticImagenesDto(Long key) {
    this(null, null, null, null, new Long(-1L), null, null, null);
    setKey(key);
  }

  public TcManticImagenesDto(String archivo, String ruta, Long tamanio, Long idUsuario, Long idImagen, Long idTipoImagen, String nombre, String alias) {
    setArchivo(archivo);
    setRuta(ruta);
    setTamanio(tamanio);
    setIdUsuario(idUsuario);
    setIdImagen(idImagen);
    setIdTipoImagen(idTipoImagen);
    setNombre(nombre);
		this.alias= alias;
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setArchivo(String archivo) {
    this.archivo = archivo;
  }

  public String getArchivo() {
    return archivo;
  }

  public void setRuta(String ruta) {
    this.ruta = ruta;
  }

  public String getRuta() {
    return ruta;
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

  public void setIdImagen(Long idImagen) {
    this.idImagen = idImagen;
  }

  public Long getIdImagen() {
    return idImagen;
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

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias=alias;
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
  	return getIdImagen();
  }

  @Override
  public void setKey(Long key) {
  	this.idImagen = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRuta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTamanio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdImagen());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoImagen());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAlias());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("archivo", getArchivo());
		regresar.put("ruta", getRuta());
		regresar.put("tamanio", getTamanio());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idImagen", getIdImagen());
		regresar.put("idTipoImagen", getIdTipoImagen());
		regresar.put("nombre", getNombre());
		regresar.put("alias", getAlias());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getArchivo(), getRuta(), getTamanio(), getIdUsuario(), getIdImagen(), getIdTipoImagen(), getNombre(), getAlias(), getRegistro()
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
    regresar.append("idImagen~");
    regresar.append(getIdImagen());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdImagen());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticImagenesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdImagen()!= null && getIdImagen()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticImagenesDto other = (TcManticImagenesDto) obj;
    if (getIdImagen() != other.idImagen && (getIdImagen() == null || !getIdImagen().equals(other.idImagen))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdImagen() != null ? getIdImagen().hashCode() : 0);
    return hash;
  }

}


