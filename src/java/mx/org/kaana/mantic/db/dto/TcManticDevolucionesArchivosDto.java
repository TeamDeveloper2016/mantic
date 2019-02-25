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
@Table(name="tc_mantic_devoluciones_archivos")
public class TcManticDevolucionesArchivosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_devolucion_archivo")
  private Long idDevolucionArchivo;
  @Column (name="id_devolucion")
  private Long idDevolucion;
  @Column (name="ruta")
  private String ruta;
  @Column (name="tamanio")
  private Long tamanio;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_tipo_archivo")
  private Long idTipoArchivo;
  @Column (name="id_principal")
  private Long idPrincipal;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="alias")
  private String alias;
  @Column (name="nombre")
  private String nombre;
  @Column (name="archivo")
  private String archivo;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticDevolucionesArchivosDto() {
    this(new Long(-1L));
  }

  public TcManticDevolucionesArchivosDto(Long key) {
    this(new Long(-1L), null, null, null, null, null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticDevolucionesArchivosDto(Long idDevolucionArchivo, Long idDevolucion, String ruta, Long tamanio, Long idUsuario, Long idTipoArchivo, Long idPrincipal, String observaciones, String alias, String nombre, String archivo) {
    setIdDevolucionArchivo(idDevolucionArchivo);
    setIdDevolucion(idDevolucion);
    setRuta(ruta);
    setTamanio(tamanio);
    setIdUsuario(idUsuario);
    setIdTipoArchivo(idTipoArchivo);
    setIdPrincipal(idPrincipal);
    setObservaciones(observaciones);
    setAlias(alias);
    setNombre(nombre);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		this.archivo= archivo;
  }
	
  public void setIdDevolucionArchivo(Long idDevolucionArchivo) {
    this.idDevolucionArchivo = idDevolucionArchivo;
  }

  public Long getIdDevolucionArchivo() {
    return idDevolucionArchivo;
  }

  public void setIdDevolucion(Long idDevolucion) {
    this.idDevolucion = idDevolucion;
  }

  public Long getIdDevolucion() {
    return idDevolucion;
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

  public void setIdPrincipal(Long idPrincipal) {
    this.idPrincipal = idPrincipal;
  }

  public Long getIdPrincipal() {
    return idPrincipal;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String getAlias() {
    return alias;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

	public String getArchivo() {
		return archivo;
	}

	public void setArchivo(String archivo) {
		this.archivo=archivo;
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
  	return getIdDevolucionArchivo();
  }

  @Override
  public void setKey(Long key) {
  	this.idDevolucionArchivo = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdDevolucionArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdDevolucion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRuta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTamanio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPrincipal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAlias());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idDevolucionArchivo", getIdDevolucionArchivo());
		regresar.put("idDevolucion", getIdDevolucion());
		regresar.put("ruta", getRuta());
		regresar.put("tamanio", getTamanio());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idTipoArchivo", getIdTipoArchivo());
		regresar.put("idPrincipal", getIdPrincipal());
		regresar.put("observaciones", getObservaciones());
		regresar.put("alias", getAlias());
		regresar.put("nombre", getNombre());
		regresar.put("archivo", getArchivo());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdDevolucionArchivo(), getIdDevolucion(), getRuta(), getTamanio(), getIdUsuario(), getIdTipoArchivo(), getIdPrincipal(), getObservaciones(), getAlias(), getNombre(), getArchivo(), getRegistro()
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
    regresar.append("idDevolucionArchivo~");
    regresar.append(getIdDevolucionArchivo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdDevolucionArchivo());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticDevolucionesArchivosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdDevolucionArchivo()!= null && getIdDevolucionArchivo()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticDevolucionesArchivosDto other = (TcManticDevolucionesArchivosDto) obj;
    if (getIdDevolucionArchivo() != other.idDevolucionArchivo && (getIdDevolucionArchivo() == null || !getIdDevolucionArchivo().equals(other.idDevolucionArchivo))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdDevolucionArchivo() != null ? getIdDevolucionArchivo().hashCode() : 0);
    return hash;
  }

}


