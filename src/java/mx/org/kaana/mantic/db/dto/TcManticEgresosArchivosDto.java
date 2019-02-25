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
@Table(name="tc_mantic_egresos_archivos")
public class TcManticEgresosArchivosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_egreso_archivo")
  private Long idEgresoArchivo;
  @Column (name="id_egreso")
  private Long idEgreso;
  @Column (name="tamanio")
  private Long tamanio;
  @Column (name="id_usuarios")
  private Long idUsuarios;
  @Column (name="id_tipo_archivo")
  private Long idTipoArchivo;
  @Column (name="id_principal")
  private Long idPrincipal;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="alias")
  private String alias;
  @Column (name="mes")
  private Long mes;
  @Column (name="nombre")
  private String nombre;
  @Column (name="archivo")
  private String archivo;
  @Column (name="ruta")
  private String ruta;
  @Column (name="ejercicio")
  private Long ejercicio;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticEgresosArchivosDto() {
    this(new Long(-1L));
  }

  public TcManticEgresosArchivosDto(Long key) {
    this(new Long(-1L), null, null, null, null, null, null, null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticEgresosArchivosDto(Long idEgresoArchivo, Long idEgreso, Long tamanio, Long idUsuarios, Long idTipoArchivo, Long idPrincipal, String observaciones, String alias, Long mes, String nombre, Long ejercicio, String ruta, String archivo) {
    setIdEgresoArchivo(idEgresoArchivo);
    setIdEgreso(idEgreso);
    setTamanio(tamanio);
    setIdUsuarios(idUsuarios);
    setIdTipoArchivo(idTipoArchivo);
    setIdPrincipal(idPrincipal);
    setObservaciones(observaciones);
    setAlias(alias);
    setMes(mes);
    setNombre(nombre);
    setEjercicio(ejercicio);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		setRuta(ruta);
		this.archivo= archivo;
  }
	
  public void setIdEgresoArchivo(Long idEgresoArchivo) {
    this.idEgresoArchivo = idEgresoArchivo;
  }

  public Long getIdEgresoArchivo() {
    return idEgresoArchivo;
  }

  public void setIdEgreso(Long idEgreso) {
    this.idEgreso = idEgreso;
  }

  public Long getIdEgreso() {
    return idEgreso;
  }

  public void setTamanio(Long tamanio) {
    this.tamanio = tamanio;
  }

  public Long getTamanio() {
    return tamanio;
  }

  public void setIdUsuarios(Long idUsuarios) {
    this.idUsuarios = idUsuarios;
  }

  public Long getIdUsuarios() {
    return idUsuarios;
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

  public void setMes(Long mes) {
    this.mes = mes;
  }

  public Long getMes() {
    return mes;
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

  public void setEjercicio(Long ejercicio) {
    this.ejercicio = ejercicio;
  }

  public Long getEjercicio() {
    return ejercicio;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}	

  @Transient
  @Override
  public Long getKey() {
  	return getIdEgresoArchivo();
  }

  @Override
  public void setKey(Long key) {
  	this.idEgresoArchivo = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdEgresoArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEgreso());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTamanio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuarios());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPrincipal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAlias());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMes());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEjercicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRuta());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idEgresoArchivo", getIdEgresoArchivo());
		regresar.put("idEgreso", getIdEgreso());
		regresar.put("tamanio", getTamanio());
		regresar.put("idUsuarios", getIdUsuarios());
		regresar.put("idTipoArchivo", getIdTipoArchivo());
		regresar.put("idPrincipal", getIdPrincipal());
		regresar.put("observaciones", getObservaciones());
		regresar.put("alias", getAlias());
		regresar.put("mes", getMes());
		regresar.put("nombre", getNombre());
		regresar.put("archivo", getArchivo());
		regresar.put("ejercicio", getEjercicio());
		regresar.put("registro", getRegistro());
		regresar.put("ruta", getRuta());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
      getIdEgresoArchivo(), getIdEgreso(), getTamanio(), getIdUsuarios(), getIdTipoArchivo(), getIdPrincipal(), getObservaciones(), getAlias(), getMes(), getNombre(), getArchivo(), getEjercicio(), getRegistro(), getRuta()
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
    regresar.append("idEgresoArchivo~");
    regresar.append(getIdEgresoArchivo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdEgresoArchivo());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticEgresosArchivosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdEgresoArchivo()!= null && getIdEgresoArchivo()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticEgresosArchivosDto other = (TcManticEgresosArchivosDto) obj;
    if (getIdEgresoArchivo() != other.idEgresoArchivo && (getIdEgresoArchivo() == null || !getIdEgresoArchivo().equals(other.idEgresoArchivo))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdEgresoArchivo() != null ? getIdEgresoArchivo().hashCode() : 0);
    return hash;
  }

}


