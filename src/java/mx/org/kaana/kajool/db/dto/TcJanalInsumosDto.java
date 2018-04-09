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

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/10/2016
 *@time 11:58:22 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Entity
@Table(name="tc_janal_insumos")
public class TcJanalInsumosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="orden")
  private Long orden;
  @Column (name="disponible")
  private Long disponible;
  @Column (name="descarga")
  private String descarga;
  @Column (name="nombre")
  private String nombre;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_insumo")
  private Long idInsumo;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="contenido")
  private String contenido;
  @Column (name="descripcion")
  private String descripcion;
  @Column (name="ruta")
  private String ruta;
  @Column (name="version")
  private String version;

  public TcJanalInsumosDto() {
    this(new Long(-1L));
  }

  public TcJanalInsumosDto(Long key) {
    this(null, null, null, new Long(-1L), null, null, null, null, null);
    setKey(key);
  }

  public TcJanalInsumosDto(Long orden, String descarga, String nombre, Long idInsumo, String contenido, String descripcion, String ruta, String version, Long disponible) {
    setOrden(orden);
    setDescarga(descarga);
    setNombre(nombre);
    setIdInsumo(idInsumo);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setContenido(contenido);
    setDescripcion(descripcion);
    setRuta(ruta);
    setVersion(version);
    setDisponible(disponible);
  }
	
  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
  }

  public void setDescarga(String descarga) {
    this.descarga = descarga;
  }

  public String getDescarga() {
    return descarga;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public void setIdInsumo(Long idInsumo) {
    this.idInsumo = idInsumo;
  }

  public Long getIdInsumo() {
    return idInsumo;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setContenido(String contenido) {
    this.contenido = contenido;
  }

  public String getContenido() {
    return contenido;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setRuta(String ruta) {
    this.ruta = ruta;
  }

  public String getRuta() {
    return ruta;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getVersion() {
    return version;
  }

  public Long getDisponible() {
    return disponible;
  }

  public void setDisponible(Long disponible) {
    this.disponible = disponible;
  }
  
  @Transient
  @Override
  public Long getKey() {
  	return getIdInsumo();
  }

  @Override
  public void setKey(Long key) {
  	this.idInsumo = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescarga());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdInsumo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getContenido());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRuta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getVersion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDisponible());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("orden", getOrden());
		regresar.put("descarga", getDescarga());
		regresar.put("nombre", getNombre());
		regresar.put("idInsumo", getIdInsumo());
		regresar.put("registro", getRegistro());
		regresar.put("contenido", getContenido());
		regresar.put("descripcion", getDescripcion());
		regresar.put("ruta", getRuta());
		regresar.put("version", getVersion());
		regresar.put("disponible", getDisponible());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getOrden(), getDescarga(), getNombre(), getIdInsumo(), getRegistro(), getContenido(), getDescripcion(), getRuta(), getVersion(), getDisponible()
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
    regresar.append("idInsumo~");
    regresar.append(getIdInsumo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdInsumo());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcJanalInsumosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdInsumo()!= null && getIdInsumo()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcJanalInsumosDto other = (TcJanalInsumosDto) obj;
    if (getIdInsumo() != other.idInsumo && (getIdInsumo() == null || !getIdInsumo().equals(other.idInsumo))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdInsumo() != null ? getIdInsumo().hashCode() : 0);
    return hash;
  }

}


