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
@Table(name="tc_mantic_listas_precios_archivos")
public class TcManticListasPreciosArchivosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_lista_precio")
  private Long idListaPrecio;
  @Column (name="ruta")
  private String ruta;
  @Column (name="tamanio")
  private Long tamanio;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_tipo_archivo")
  private Long idTipoArchivo;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="id_principal")
  private Long idPrincipal;
  @Column (name="alias")
  private String alias;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_lista_precio_archivo")
  private Long idListaPrecioArchivo;
  @Column (name="nombre")
  private String nombre;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticListasPreciosArchivosDto() {
    this(new Long(-1L));
  }

  public TcManticListasPreciosArchivosDto(Long key) {
    this(null, null, null, null, null, null, null, null, new Long(-1L), null);
    setKey(key);
  }

  public TcManticListasPreciosArchivosDto(Long idListaPrecio, String ruta, Long tamanio, Long idUsuario, Long idTipoArchivo, String observaciones, Long idPrincipal, String alias, Long idListaPrecioArchivo, String nombre) {
    setIdListaPrecio(idListaPrecio);
    setRuta(ruta);
    setTamanio(tamanio);
    setIdUsuario(idUsuario);
    setIdTipoArchivo(idTipoArchivo);
    setObservaciones(observaciones);
    setIdPrincipal(idPrincipal);
    setAlias(alias);
    setIdListaPrecioArchivo(idListaPrecioArchivo);
    setNombre(nombre);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdListaPrecio(Long idListaPrecio) {
    this.idListaPrecio = idListaPrecio;
  }

  public Long getIdListaPrecio() {
    return idListaPrecio;
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

  public void setIdTipoArchivo(Long idTipoArchivo) {
    this.idTipoArchivo = idTipoArchivo;
  }

  public Long getIdTipoArchivo() {
    return idTipoArchivo;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setIdPrincipal(Long idPrincipal) {
    this.idPrincipal = idPrincipal;
  }

  public Long getIdPrincipal() {
    return idPrincipal;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String getAlias() {
    return alias;
  }

  public void setIdListaPrecioArchivo(Long idListaPrecioArchivo) {
    this.idListaPrecioArchivo = idListaPrecioArchivo;
  }

  public Long getIdListaPrecioArchivo() {
    return idListaPrecioArchivo;
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
  	return getIdListaPrecioArchivo();
  }

  @Override
  public void setKey(Long key) {
  	this.idListaPrecioArchivo = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdListaPrecio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRuta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTamanio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPrincipal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAlias());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdListaPrecioArchivo());
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
		regresar.put("idListaPrecio", getIdListaPrecio());
		regresar.put("ruta", getRuta());
		regresar.put("tamanio", getTamanio());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idTipoArchivo", getIdTipoArchivo());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idPrincipal", getIdPrincipal());
		regresar.put("alias", getAlias());
		regresar.put("idListaPrecioArchivo", getIdListaPrecioArchivo());
		regresar.put("nombre", getNombre());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdListaPrecio(), getRuta(), getTamanio(), getIdUsuario(), getIdTipoArchivo(), getObservaciones(), getIdPrincipal(), getAlias(), getIdListaPrecioArchivo(), getNombre(), getRegistro()
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
    regresar.append("idListaPrecioArchivo~");
    regresar.append(getIdListaPrecioArchivo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdListaPrecioArchivo());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticListasPreciosArchivosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdListaPrecioArchivo()!= null && getIdListaPrecioArchivo()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticListasPreciosArchivosDto other = (TcManticListasPreciosArchivosDto) obj;
    if (getIdListaPrecioArchivo() != other.idListaPrecioArchivo && (getIdListaPrecioArchivo() == null || !getIdListaPrecioArchivo().equals(other.idListaPrecioArchivo))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdListaPrecioArchivo() != null ? getIdListaPrecioArchivo().hashCode() : 0);
    return hash;
  }

}


