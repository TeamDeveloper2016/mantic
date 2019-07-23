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

@Entity
@Table(name="tc_mantic_descargas")
public class TcManticDescargasDto implements IBaseDto, Serializable {
	
	private static final long serialVersionUID=1L;
  @Column (name="ruta")
  private String ruta;
  @Column (name="tamanio")
  private Long tamanio;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="observaciones")
  private String observaciones;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_descarga")
  private Long idDescarga;
  @Column (name="alias")
  private String alias;
  @Column (name="nombre")
  private String nombre;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="eliminado")
  private Timestamp eliminado;
	@Column (name="activo")
  private Long activo;

  public TcManticDescargasDto() {
    this(new Long(-1L));
  }

  public TcManticDescargasDto(Long key) {
    this(null, null, null, null, new Long(-1L), null, null);
    setKey(key);
  }

  public TcManticDescargasDto(String ruta, Long tamanio, Long idUsuario, String observaciones, Long idDescarga, String alias, String nombre) {
		this(ruta, tamanio, idUsuario, observaciones, idDescarga, alias, nombre, 1L);
	}
	
  public TcManticDescargasDto(String ruta, Long tamanio, Long idUsuario, String observaciones, Long idDescarga, String alias, String nombre, Long activo) {
    setRuta(ruta);
    setTamanio(tamanio);
    setIdUsuario(idUsuario);
    setObservaciones(observaciones);
    setIdDescarga(idDescarga);
    setAlias(alias);
    setNombre(nombre);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		setEliminado(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		setActivo(activo);
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

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setIdDescarga(Long idDescarga) {
    this.idDescarga = idDescarga;
  }

  public Long getIdDescarga() {
    return idDescarga;
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

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

	public Timestamp getEliminado() {
		return eliminado;
	}

	public void setEliminado(Timestamp eliminado) {
		this.eliminado = eliminado;
	}

	public Long getActivo() {
		return activo;
	}

	public void setActivo(Long activo) {
		this.activo = activo;
	}	
	
  @Transient
  @Override
  public Long getKey() {
  	return getIdDescarga();
  }

  @Override
  public void setKey(Long key) {
  	this.idDescarga = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getRuta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTamanio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdDescarga());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAlias());
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
		regresar.put("ruta", getRuta());
		regresar.put("tamanio", getTamanio());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idDescarga", getIdDescarga());
		regresar.put("alias", getAlias());
		regresar.put("nombre", getNombre());
		regresar.put("registro", getRegistro());
		regresar.put("eliminado", getEliminado());
		regresar.put("activo", getActivo());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getRuta(), getTamanio(), getIdUsuario(), getObservaciones(), getIdDescarga(), getAlias(), getNombre(), getRegistro(), getEliminado(), getActivo()
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
    regresar.append("idDescarga~");
    regresar.append(getIdDescarga());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdDescarga());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticDescargasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdDescarga()!= null && getIdDescarga()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticDescargasDto other = (TcManticDescargasDto) obj;
    if (getIdDescarga() != other.idDescarga && (getIdDescarga() == null || !getIdDescarga().equals(other.idDescarga))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdDescarga() != null ? getIdDescarga().hashCode() : 0);
    return hash;
  }
}
