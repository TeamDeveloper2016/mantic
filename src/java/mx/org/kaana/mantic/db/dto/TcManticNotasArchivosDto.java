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
@Table(name="tc_mantic_notas_archivos")
public class TcManticNotasArchivosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_nota_archivo")
  private Long idNotaArchivo;
  @Column (name="ruta")
  private String ruta;
  @Column (name="tamanio")
  private Long tamanio;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_tipo_archivo")
  private Long idTipoArchivo;
  @Column (name="alias")
  private String alias;
  @Column (name="mes")
  private Long mes;
  @Column (name="id_nota_entrada")
  private Long idNotaEntrada;
  @Column (name="nombre")
  private String nombre;
  @Column (name="archivo")
  private String archivo;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="ejercicio")
  private Long ejercicio;
  @Column (name="id_principal")
  private Long idPrincipal;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticNotasArchivosDto() {
    this(new Long(-1L));
  }

  public TcManticNotasArchivosDto(Long key) {
    this(new Long(-1L), null, null, null, null, null, null, null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticNotasArchivosDto(Long idNotaArchivo, String ruta, Long tamanio, Long idUsuario, Long idTipoArchivo, String alias, Long mes, Long idNotaEntrada, String nombre, String observaciones, Long ejercicio, Long idPrincipal, String archivo) {
    setIdNotaArchivo(idNotaArchivo);
    setRuta(ruta);
    setTamanio(tamanio);
    setIdUsuario(idUsuario);
    setIdTipoArchivo(idTipoArchivo);
    setAlias(alias);
    setMes(mes);
    setIdNotaEntrada(idNotaEntrada);
    setNombre(nombre);
    setObservaciones(observaciones);
    setEjercicio(ejercicio);
    setIdPrincipal(idPrincipal);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		this.archivo= archivo;
  }
	
  public void setIdNotaArchivo(Long idNotaArchivo) {
    this.idNotaArchivo = idNotaArchivo;
  }

  public Long getIdNotaArchivo() {
    return idNotaArchivo;
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

  public void setIdNotaEntrada(Long idNotaEntrada) {
    this.idNotaEntrada = idNotaEntrada;
  }

  public Long getIdNotaEntrada() {
    return idNotaEntrada;
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

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setEjercicio(Long ejercicio) {
    this.ejercicio = ejercicio;
  }

  public Long getEjercicio() {
    return ejercicio;
  }

	public Long getIdPrincipal() {
		return idPrincipal;
	}

	public void setIdPrincipal(Long idPrincipal) {
		this.idPrincipal=idPrincipal;
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
  	return getIdNotaArchivo();
  }

  @Override
  public void setKey(Long key) {
  	this.idNotaArchivo = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdNotaArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRuta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTamanio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAlias());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMes());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdNotaEntrada());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEjercicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPrincipal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idNotaArchivo", getIdNotaArchivo());
		regresar.put("ruta", getRuta());
		regresar.put("tamanio", getTamanio());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idTipoArchivo", getIdTipoArchivo());
		regresar.put("alias", getAlias());
		regresar.put("mes", getMes());
		regresar.put("idNotaEntrada", getIdNotaEntrada());
		regresar.put("nombre", getNombre());
		regresar.put("archivo", getArchivo());
		regresar.put("observaciones", getObservaciones());
		regresar.put("ejercicio", getEjercicio());
		regresar.put("idPrincipal", getIdPrincipal());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdNotaArchivo(), getRuta(), getTamanio(), getIdUsuario(), getIdTipoArchivo(), getAlias(), getMes(), getIdNotaEntrada(), getNombre(), getArchivo(), getObservaciones(), getEjercicio(), getIdPrincipal(), getRegistro()
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
    regresar.append("idNotaArchivo~");
    regresar.append(getIdNotaArchivo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdNotaArchivo());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticNotasArchivosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdNotaArchivo()!= null && getIdNotaArchivo()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticNotasArchivosDto other = (TcManticNotasArchivosDto) obj;
    if (getIdNotaArchivo() != other.idNotaArchivo && (getIdNotaArchivo() == null || !getIdNotaArchivo().equals(other.idNotaArchivo))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdNotaArchivo() != null ? getIdNotaArchivo().hashCode() : 0);
    return hash;
  }

}


